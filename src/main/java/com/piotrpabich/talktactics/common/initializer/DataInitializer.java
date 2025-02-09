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
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
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
    @Override
    public void run(ApplicationArguments args) {
        Instant start = Instant.now();
        log.info("Initializing data... | {}", start);

//        initData();
//        Instant end = Instant.now();
//
//        Duration duration = Duration.between(start, end);

//        log.info("Data initialized successfully! | Took: {} seconds | {}", duration.toMillis() / 1000.0, end);
    }

    private void initData() {
        initUsers();
        initCourses();
        initCourseItems();
        initUserCourses();
    }
    private void initUsers() {
        userList.add(User.builder().username("admin").password(passwordEncoder.encode("admin")).email("email@gmail.com").firstName("Piotr").lastName("Pabich").bio("Passionate about technology, design, and the power of innovation. Always seeking new challenges and ways to make an impact.").role(Role.ADMIN).build());
        userList.add(User.builder().username("user").password(passwordEncoder.encode("user")).email("user@gmail.com").firstName("Jan").lastName("Tomczyk").bio("Adventurous soul, chasing dreams one step at a time. Lover of art, nature, and good conversations. Here to make memories").role(Role.USER).build());
        userList.add(User.builder().username("user1").password(passwordEncoder.encode("user1")).email("user1@gmail.com").firstName("Tomasz").lastName("Kukułka").bio("Avid reader, passionate writer, and eternal optimist. Finding beauty in the little things and spreading positivity wherever I go.").role(Role.USER).build());

        for(int i = 0; i < USERS_SIZE; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String username = String.format("%s%s%d", firstName, lastName, i);
            String email = String.format("%s%s%d@email.com", firstName, lastName, i);
            String bio = UserProfileBioGenerator.generateBio(random.nextInt(3) + 1);
            userList.add(User.builder().username(username).password(passwordEncoder.encode("talktactics")).email(email).firstName(firstName).lastName(lastName).bio(bio).role(Role.USER).totalPoints(0).build());
        }
        userRepository.saveAll(userList);
    }
    private void initCourses() {
        for(int i = 0; i < COURSES_SIZE; i++) {
            String name = CourseDataGenerator.selectCourseTitle(i);
            String description = CourseDataGenerator.selectCourseDescription(i);
            CourseLevel level = i < COURSES_SIZE / 4 ? CourseLevel.BEGINNER : i < COURSES_SIZE * 3/4 ? CourseLevel.INTERMEDIATE : CourseLevel.ADVANCED;
            courseList.add(Course.builder().title(name).description(description).level(level).build());
        }
        courseRepository.saveAll(courseList);
    }
    private void initCourseItems() {
        try {
            JsonNode rootNode = readJsonFile("src/main/java/com/piotrpabich/talktactics/common/initializer/long_words.json");

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
                        course.setLevel(course.getLevel());
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
                courseItem.setLevel(course.getLevel());
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
                UserCourse userCourse = UserCourse.builder().user(user).course(course).build();
                List<UserCourseItem> userCourseItems = new ArrayList<>();
                for(CourseItem courseItem: course.getCourseItems()) {
                    userCourseItems.add(UserCourseItem.builder().courseItem(courseItem).isLearned(random.nextBoolean()).userCourse(userCourse).build());
                }
                userCourse.setUserCourseItems(userCourseItems);
                userCourseList.add(userCourse);
            });
        }
        userCourseRepository.saveAll(userCourseList);
    }
    private JsonNode readJsonFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filePath);
        return objectMapper.readTree(jsonFile);
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
            List<CourseItem> items = courseItemList.stream().filter(item -> item.getCourse().getId() == course.getId()).toList();
            course.setCourseItems(items);
        }
    }
}
