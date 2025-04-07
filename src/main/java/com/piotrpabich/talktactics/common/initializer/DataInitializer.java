package com.piotrpabich.talktactics.common.initializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piotrpabich.talktactics.course.CourseDataGenerator;
import com.piotrpabich.talktactics.course.CourseRepository;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.course.participant.CourseParticipantRepository;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import com.piotrpabich.talktactics.course.participant.word.CourseParticipantWordRepository;
import com.piotrpabich.talktactics.course.participant.word.entity.CourseParticipantWord;
import com.piotrpabich.talktactics.course.word.CourseWordDefinitionRepository;
import com.piotrpabich.talktactics.course.word.CourseWordRepository;
import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import com.piotrpabich.talktactics.course.word.entity.CourseWordDefinition;
import com.piotrpabich.talktactics.user.UserProfileBioGenerator;
import com.piotrpabich.talktactics.user.UserRepository;
import com.piotrpabich.talktactics.user.entity.Role;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@Transactional
@Log4j2
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final int USERS_SIZE = 50;
    private static final int COURSES_SIZE = 50;
    private static final int BEGINNER_COURSES_BREAKPOINT = 15;
    private static final int INTERMEDIATE_COURSES_BREAKPOINT = 35;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseWordDefinitionRepository courseWordDefinitionRepository;
    private final CourseWordRepository courseWordRepository;
    private final CourseParticipantRepository courseParticipantRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();
    private final Random random = new Random();
    private final ArrayList<User> userList = new ArrayList<>();
    private final ArrayList<Course> courseList = new ArrayList<>();
    private final ArrayList<CourseWord> courseWordList = new ArrayList<>();
    private final CourseParticipantWordRepository courseParticipantWordRepository;

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
        initCourseWords();
        initCourseParticipants();
    }

    private void initUsers() {
        UserInitializer userInitializer = new UserInitializer(userRepository, passwordEncoder, faker);
        userList.addAll(userInitializer.initializeUsers(USERS_SIZE));
    }

    private void initCourses() {
        CourseInitializer courseInitializer = new CourseInitializer();
        courseList.addAll(courseInitializer.initializeCourses(COURSES_SIZE));
        courseRepository.saveAll(courseList);
    }

    private void initCourseWords() {
        CourseWordInitializer courseWordInitializer = new CourseWordInitializer(courseWordRepository, courseWordDefinitionRepository, courseList, random);
        try {
            courseWordList.addAll(courseWordInitializer.initializeCourseWords(readJsonFile(), COURSES_SIZE, BEGINNER_COURSES_BREAKPOINT, INTERMEDIATE_COURSES_BREAKPOINT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCourseParticipants() {
        CourseParticipantInitializer participantInitializer = new CourseParticipantInitializer(courseParticipantRepository, courseParticipantWordRepository, courseList, random);
        participantInitializer.initializeCourseParticipants(userList);
        userRepository.saveAllAndFlush(userList);
    }

    private JsonNode readJsonFile() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/long_words.json");
        if (inputStream == null) {
            System.err.println("Resource not found: long_words.json");
            return null;
        }
        return new ObjectMapper().readTree(inputStream);
    }

    @RequiredArgsConstructor
    private static class UserInitializer {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final Faker faker;

        public List<User> initializeUsers(int usersSize) {
            List<User> users = new ArrayList<>();
            users.add(createUser("Piotr", "Pabich", "admin", "email@gmail.com", "Passionate about technology, design, and the power of innovation. Always seeking new challenges and ways to make an impact.", Role.ADMIN));
            users.add(createUser("Jan", "Tomczyk", "user", "user@gmail.com", "Adventurous soul, chasing dreams one step at a time. Lover of art, nature, and good conversations. Here to make memories", Role.USER));
            users.add(createUser("Tomasz", "Kukułka", "user1", "user1@gmail.com", "Avid reader, passionate writer, and eternal optimist. Finding beauty in the little things and spreading positivity wherever I go.", Role.USER));

            IntStream.range(0, usersSize).forEach(i -> users.add(createUser(i)));
            userRepository.saveAll(users);
            return users;
        }

        private User createUser(int index) {
            final var firstName = faker.name().firstName();
            final var lastName = faker.name().lastName();
            final var username = String.format("%s%s%d", firstName, lastName, index);
            final var email = String.format("%s%s%d@email.com", firstName, lastName, index);
            final var bio = UserProfileBioGenerator.generateBio(new Random().nextInt(3) + 1);
            return createUser(firstName, lastName, username, email, bio, Role.USER);
        }

        private User createUser(String firstName, String lastName, String username, String email, String bio, Role role) {
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
    }

    private static class CourseInitializer {

        public List<Course> initializeCourses(int coursesSize) {
            List<Course> courses = new ArrayList<>();
            for (int i = 0; i < coursesSize; i++) {
                String title = CourseDataGenerator.selectCourseTitle(i);
                String description = CourseDataGenerator.selectCourseDescription(i);
                CourseLevel level = i < coursesSize / 4 ? CourseLevel.BEGINNER : i < coursesSize * 3 / 4 ? CourseLevel.INTERMEDIATE : CourseLevel.ADVANCED;
                final var course = new Course();
                course.setTitle(title);
                course.setDescription(description);
                course.setLevel(level);
                courses.add(course);
            }
            return courses;
        }
    }

    @RequiredArgsConstructor
    private static class CourseWordInitializer {
        private final CourseWordRepository courseWordRepository;
        private final CourseWordDefinitionRepository courseWordDefinitionRepository;
        private final List<Course> courseList;
        private final Random random;

        public List<CourseWord> initializeCourseWords(JsonNode rootNode, int coursesSize, int beginnerBreakpoint, int intermediateBreakpoint) throws IOException {
            List<CourseWord> courseWords = new ArrayList<>();
            if (rootNode == null) {
                return courseWords;
            }

            int totalItems = rootNode.size();
            int itemsPerCourse = totalItems / coursesSize;
            int itemsAssigned = 0;
            for (int i = 0; i < coursesSize; i++) {
                Course course = courseList.get(i);
                int courseItemsCount = 0;
                int maxWords, minWords;

                if (i < beginnerBreakpoint) {
                    minWords = 20;
                    maxWords = itemsPerCourse / 3 + random.nextInt(itemsPerCourse / 3);
                } else if (i < intermediateBreakpoint) {
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
                        CourseWord courseWord = createCourseItem(courseItemNode);
                        courseWord.setCourse(course);
                        courseWords.add(courseWord);
                        courseItemsCount++;
                        itemsAssigned++;
                    } else {
                        break;
                    }
                }
            }

            int remainingItems = totalItems - itemsAssigned;
            for (int i = 0; i < remainingItems; i++) {
                JsonNode courseItemNode = rootNode.get(itemsAssigned).get(0);
                CourseWord courseWord = createCourseItem(courseItemNode);
                Course course = courseList.get(random.nextInt(coursesSize));
                courseWord.setCourse(course);
                courseWords.add(courseWord);
                itemsAssigned++;
            }

            assignCourseItemsToCourses(courseList, courseWords);
            courseWordRepository.saveAll(courseWords);
            return courseWords;
        }

        private CourseWord createCourseItem(JsonNode itemNode) {
            String word = itemNode.hasNonNull("word") ? itemNode.get("word").asText() : null;
            String phonetic = itemNode.hasNonNull("phonetic") ? itemNode.get("phonetic").asText() : null;
            String audio = getAudioFromPhoneticsNode(itemNode.get("phonetics"));

            CourseWord courseWord = new CourseWord();
            courseWord.setWord(word);
            courseWord.setPhonetic(phonetic);
            courseWord.setAudio(audio);
            courseWordRepository.save(courseWord);

            JsonNode meaningsNode = itemNode.get("meanings");
            String partOfSpeech = meaningsNode.get(0).get("partOfSpeech").asText();
            courseWord.setPartOfSpeech(partOfSpeech);

            List<CourseWordDefinition> courseWordDefinitions = createMeanings(meaningsNode.get(0).get("definitions"), courseWord);
            courseWordDefinitionRepository.saveAll(courseWordDefinitions);

            return courseWord;
        }

        private String getAudioFromPhoneticsNode(JsonNode phoneticsNode) {
            String audio = null;
            for (JsonNode phoneticNode : phoneticsNode) {
                if (phoneticNode.hasNonNull("audio")) {
                    String newAudio = phoneticNode.get("audio").asText();
                    if (!newAudio.isBlank()) {
                        audio = newAudio;
                        break;
                    }
                }
            }
            return audio;
        }

        private List<CourseWordDefinition> createMeanings(JsonNode definitionsNode, CourseWord courseWord) {
            List<CourseWordDefinition> courseWordDefinitions = new ArrayList<>();
            for (JsonNode definitionNode : definitionsNode) {
                String definition = definitionNode.hasNonNull("definition") ? definitionNode.get("definition").asText() : null;
                String example = definitionNode.hasNonNull("example") ? definitionNode.get("example").asText() : null;

                CourseWordDefinition courseWordDefinition = new CourseWordDefinition();
                courseWordDefinition.setDefinition(definition);
                courseWordDefinition.setExample(example);
                courseWordDefinition.setCourseWord(courseWord);
                courseWordDefinitions.add(courseWordDefinition);
            }
            return courseWordDefinitions;
        }

        private void assignCourseItemsToCourses(List<Course> courseList, List<CourseWord> courseWordList) {
            for (Course course : courseList) {
                List<CourseWord> items = courseWordList.stream().filter(item -> Objects.equals(item.getCourse().getId(), course.getId())).toList();
                course.setCourseWords(items);
            }
        }
    }

    @RequiredArgsConstructor
    private static class CourseParticipantInitializer {
        private final CourseParticipantRepository courseParticipantRepository;
        private final CourseParticipantWordRepository courseParticipantWordRepository;
        private final List<Course> courseList;
        private final Random random;

        public void initializeCourseParticipants(List<User> userList) {
            for (User user : userList) {
                random.ints(0, courseList.size()).distinct().limit(random.nextInt(COURSES_SIZE / 2) + 1).forEach(value -> {
                    Course course = courseList.get(value);
                    CourseParticipant courseParticipant = new CourseParticipant(user, course);
                    user.getCourseParticipants().add(courseParticipant);
                    courseParticipantRepository.save(courseParticipant);
                    List<CourseParticipantWord> courseParticipantWords = new ArrayList<>();
                    for (CourseWord courseWord : course.getCourseWords()) {
                        final var userCourseItem = new CourseParticipantWord(courseParticipant, courseWord);
                        userCourseItem.setLearned(random.nextBoolean());
                        courseParticipantWords.add(userCourseItem);
                    }
                    courseParticipant.setCourseParticipantWords(courseParticipantWords);
                    courseParticipant.setCompleted(isCompleted(courseParticipant));
                    courseParticipant.setPoints(calculateTotalPoints(courseParticipant));
                    courseParticipant.setProgress(calculateProgress(courseParticipant));
                    courseParticipantWordRepository.saveAll(courseParticipantWords);
                });
                updateUserTotalPoints(user);
            }
        }

        private void updateUserTotalPoints(final User user) {
            final var totalPoints = user.getCourseParticipants().stream()
                    .mapToInt(CourseParticipant::getPoints)
                    .sum();
            user.setTotalPoints(totalPoints);
        }

        private boolean isCompleted(final CourseParticipant courseParticipant) {
            return courseParticipant.getCourseParticipantWords()
                    .stream()
                    .allMatch(CourseParticipantWord::isLearned);
        }

        private int calculateTotalPoints(final CourseParticipant courseParticipant) {
            return courseParticipant.getCourseParticipantWords().stream()
                    .filter(CourseParticipantWord::isLearned)
                    .mapToInt(item -> item.getCourseWord().getPoints())
                    .sum() + (courseParticipant.getCompleted() ? courseParticipant.getPoints() : 0);
        }

        private double calculateProgress(final CourseParticipant courseParticipant) {
            final var totalItems = courseParticipant.getCourseParticipantWords().size();
            final var learnedItems = (int) courseParticipant.getCourseParticipantWords().stream()
                    .filter(CourseParticipantWord::isLearned)
                    .count();
            final var progress = 100.0 * learnedItems / totalItems;
            return Math.floor(progress * 10) / 10;
        }
    }

}
