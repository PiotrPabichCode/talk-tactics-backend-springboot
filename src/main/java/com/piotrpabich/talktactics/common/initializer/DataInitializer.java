package com.piotrpabich.talktactics.common.initializer;

import com.piotrpabich.talktactics.course.CourseRepository;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.course_item.CourseItemRepository;
import com.piotrpabich.talktactics.course_item.MeaningRepository;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.course_item.entity.Meaning;
import com.piotrpabich.talktactics.course.CourseDataGenerator;
import com.piotrpabich.talktactics.user.UserRepository;
import com.piotrpabich.talktactics.user.entity.Role;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.UserProfileBioGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrpabich.talktactics.user_course.UserCourseRepository;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.user_course_item.UserCourseItemRepository;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final MeaningRepository meaningRepository;
    private final CourseItemRepository courseItemRepository;
    private final UserCourseRepository userCourseRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final ArrayList<User> userList = new ArrayList<>();
    private final ArrayList<Course> courseList = new ArrayList<>();
    private final ArrayList<CourseItem> courseItemList = new ArrayList<>();
    private final ArrayList<UserCourse> userCourseList = new ArrayList<>();
    private static final int USERS_SIZE = 50;
    private static final int COURSES_SIZE = 50;
    private static final int BEGINNER_COURSES_BREAKPOINT = 15;
    private static final int INTERMEDIATE_COURSES_BREAKPOINT = 35;
    private final UserCourseItemRepository userCourseItemRepository;

    @Override
    public void run(ApplicationArguments args) {
        Instant start = Instant.now();
        log.info("Initializing data... | {}", start);

        initData();
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);

        log.info("Data initialized successfully! | Took: {} seconds | {}", duration.toMillis() / 1000.0, end);
    }

    private void initData() {
        initUsers();
        initCourses();
        initCourseItems();
        initUserCourses();
    }
    private void initUsers() {
        userList.add(addUser("Piotr", "Pabich", "admin", "email@gmail.com", "Passionate about technology, design, and the power of innovation. Always seeking new challenges and ways to make an impact.", Role.ADMIN));
        userList.add(addUser("Jan", "Tomczyk", "user", "user@gmail.com", "Adventurous soul, chasing dreams one step at a time. Lover of art, nature, and good conversations. Here to make memories", Role.USER));
        userList.add(addUser("Tomasz", "Kukułka", "user1", "user1@gmail.com", "Avid reader, passionate writer, and eternal optimist. Finding beauty in the little things and spreading positivity wherever I go.", Role.USER));

        for(int i = 0; i < USERS_SIZE; i++) {
            userList.add(addUser(i));
        }
        userRepository.saveAll(userList);
    }

    private User addUser(final Integer index) {
        final var firstName = faker.name().firstName();
        final var lastName = faker.name().lastName();
        final var username = String.format("%s%s%d", firstName, lastName, index);
        final var email = String.format("%s%s%d@email.com", firstName, lastName, index);
        final var bio = UserProfileBioGenerator.generateBio(random.nextInt(3) + 1);
        return addUser(firstName, lastName, username, email, bio, Role.USER);
    }

    private User addUser(final String firstName, final String lastName, final String username, final String email, final String bio, final Role role) {
        final var user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setBio(bio);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode("12345678"));
        return user;
    }

    private void initCourses() {
        for(int i = 0; i < COURSES_SIZE; i++) {
            String title = CourseDataGenerator.selectCourseTitle(i);
            String description = CourseDataGenerator.selectCourseDescription(i);
            CourseLevel level = i < COURSES_SIZE / 4 ? CourseLevel.BEGINNER : i < COURSES_SIZE * 3/4 ? CourseLevel.INTERMEDIATE : CourseLevel.ADVANCED;
            final var course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setLevel(level);
            courseList.add(course);
        }
        courseRepository.saveAll(courseList);
    }

    private void initCourseItems() {
        try {
            JsonNode rootNode = readJsonFile();
            if(rootNode == null) {
                return;
            }

            int totalItems = rootNode.size();
            int itemsPerCourse = totalItems / COURSES_SIZE;
            int itemsAssigned = 0;
            for (int i = 0; i < COURSES_SIZE; i++) {
                Course course = courseList.get(i);
                int courseItemsCount = 0;
                int maxWords, minWords;

                if (i < BEGINNER_COURSES_BREAKPOINT) {
                    minWords = 20;
                    maxWords = itemsPerCourse / 3 + random.nextInt(itemsPerCourse / 3);
                } else if (i < INTERMEDIATE_COURSES_BREAKPOINT) {
                    minWords = 30;
                    maxWords = itemsPerCourse / 2 + random.nextInt(itemsPerCourse / 2);
                } else {
                    minWords = 60;
                    maxWords = itemsPerCourse;
                }

                maxWords = Math.max(minWords, maxWords);

                while (itemsAssigned < totalItems && courseItemsCount < itemsPerCourse) {
                    if (courseItemsCount < maxWords) {
                        JsonNode courseItemNode = rootNode.get(itemsAssigned).get(0);
                        CourseItem courseItem = createCourseItem(courseItemNode);
                        courseItem.setCourse(course);
                        courseItemList.add(courseItem);
                        courseItemsCount++;
                        itemsAssigned++;
                    } else {
                        break;
                    }
                }
            }

            int remainingItems = totalItems - itemsAssigned;
            for(int i = 0; i < remainingItems; i++) {
                JsonNode courseItemNode = rootNode.get(itemsAssigned).get(0);
                CourseItem courseItem = createCourseItem(courseItemNode);
                Course course = courseList.get(random.nextInt(COURSES_SIZE));
                courseItem.setCourse(course);
                courseItemList.add(courseItem);
                itemsAssigned++;
            }

            assignCourseItemsToCourses();
            courseItemRepository.saveAll(courseItemList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initUserCourses() {
        for(User user: userList) {
            random.ints(0, courseList.size()).distinct().limit(random.nextInt(COURSES_SIZE / 2) + 1).forEach(value -> {
                Course course = courseList.get(value);
                UserCourse userCourse = new UserCourse(user, course);
                user.getUserCourses().add(userCourse);
                userCourseRepository.save(userCourse);
                List<UserCourseItem> userCourseItems = new ArrayList<>();
                for(CourseItem courseItem: course.getCourseItems()) {
                    final var userCourseItem = new UserCourseItem(userCourse, courseItem);
                    userCourseItem.setLearned(random.nextBoolean());
                    userCourseItems.add(userCourseItem);
                }
                userCourse.setUserCourseItems(userCourseItems);
                userCourse.setCompleted(isCompleted(userCourse));
                userCourse.setPoints(calculateTotalPoints(userCourse));
                userCourse.setProgress(calculateProgress(userCourse));
                userCourseItemRepository.saveAll(userCourseItems);
            });
            updateUserTotalPoints(user);
        }
        userRepository.saveAllAndFlush(userList);
    }

    private void updateUserTotalPoints(final User user) {
        final var totalPoints = user.getUserCourses().stream()
                .mapToInt(UserCourse::getPoints)
                .sum();
        user.setTotalPoints(totalPoints);
    }

    private boolean isCompleted(final UserCourse userCourse) {
        return userCourse.getUserCourseItems()
                .stream()
                .allMatch(UserCourseItem::isLearned);
    }

    private int calculateTotalPoints(final UserCourse userCourse) {
        return userCourse.getUserCourseItems().stream()
                .filter(UserCourseItem::isLearned)
                .mapToInt(item -> item.getCourseItem().getPoints())
                .sum() + (userCourse.getCompleted() ? userCourse.getPoints() : 0);
    }

    private double calculateProgress(final UserCourse userCourse) {
        final var totalItems = userCourse.getUserCourseItems().size();
        final var learnedItems = (int) userCourse.getUserCourseItems().stream()
                .filter(UserCourseItem::isLearned)
                .count();
        final var progress = 100.0 * learnedItems / totalItems;
        return Math.floor(progress * 10) / 10;
    }

    private JsonNode readJsonFile() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                "data/long_words.json"
        );

        if (inputStream == null) {
            System.err.println("Resource not found: long_words.json");
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(inputStream);
    }
    private CourseItem createCourseItem(JsonNode itemNode) {
        String word = itemNode.hasNonNull("word") ? itemNode.get("word").asText() : null;
        String phonetic = itemNode.hasNonNull("phonetic") ? itemNode.get("phonetic").asText() : null;
        String audio = getAudioFromPhoneticsNode(itemNode.get("phonetics"));

        CourseItem courseItem = CourseItem.builder()
                .word(word)
                .phonetic(phonetic)
                .audio(audio)
                .build();
        courseItemRepository.save(courseItem);

        JsonNode meaningsNode = itemNode.get("meanings");
        String partOfSpeech = meaningsNode.get(0).get("partOfSpeech").asText();
        courseItem.setPartOfSpeech(partOfSpeech);

        List<Meaning> meanings = createMeanings(meaningsNode.get(0).get("definitions"), courseItem);
        meaningRepository.saveAll(meanings);

        return courseItem;
    }
    private String getAudioFromPhoneticsNode(JsonNode phoneticsNode) {
        String audio = null;
        for(JsonNode phoneticNode: phoneticsNode) {
            if(phoneticNode.hasNonNull("audio")) {
                String newAudio = phoneticNode.get("audio").asText();
                if(!newAudio.isBlank()) {
                    audio = newAudio;
                    break;
                }
            }
        }
        return audio;
    }
    private List<Meaning> createMeanings(JsonNode definitionsNode, CourseItem courseItem) {
        List<Meaning> meanings = new ArrayList<>();
        for (JsonNode definitionNode : definitionsNode) {
            String definition = definitionNode.hasNonNull("definition") ? definitionNode.get("definition").asText() : null;
            String example = definitionNode.hasNonNull("example") ? definitionNode.get("example").asText() : null;

            Meaning meaning = Meaning.builder()
                    .definition(definition)
                    .example(example)
                    .courseItem(courseItem)
                    .build();

            meanings.add(meaning);
        }
        return meanings;
    }
    private void assignCourseItemsToCourses() {
        for(Course course: courseList) {
            List<CourseItem> items = courseItemList.stream().filter(item -> Objects.equals(item.getCourse().getId(), course.getId())).toList();
            course.setCourseItems(items);
        }
    }
}
