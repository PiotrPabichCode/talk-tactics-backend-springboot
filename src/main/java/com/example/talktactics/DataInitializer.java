package com.example.talktactics;

import com.example.talktactics.entity.*;
import com.example.talktactics.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@Transactional
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeaningRepository meaningRepository;
    @Autowired
    private CourseItemRepository courseItemRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private UserCourseItemRepository userCourseItemRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private void loadCourseItemsFromJson(List<Course> courses) {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/main/java/com/example/talktactics/util/long_words.json");

        int counter = 0;
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonFile);
            List<CourseItem> courseItems = new ArrayList<>();

            for (JsonNode courseItemNode : jsonNode) {
                JsonNode item = courseItemNode.get(0);
                String word = item.hasNonNull("word") ? item.get("word").asText() : null;
                String phonetic = item.hasNonNull("phonetic") ? item.get("phonetic").asText() : null;
                JsonNode phoneticsNode = item.get("phonetics");
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

                CourseItem courseItem = CourseItem.builder()
                        .word(word)
                        .phonetic(phonetic)
                        .audio(audio)
                        .build();

                JsonNode meaningsNode = item.get("meanings");
                String partOfSpeech = meaningsNode.get(0).get("partOfSpeech").asText();
                courseItem.setPartOfSpeech(partOfSpeech);
                JsonNode definitionsNode = meaningsNode.get(0).get("definitions");
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
                meaningRepository.saveAll(meanings);

                Course course = courses.get(counter / 100);
                courseItem.setCourse(course);
                courseItems.add(courseItem);
                counter++;
            }
            for(Course course: courses) {
                List<CourseItem> items = courseItems.stream().filter(item -> item.getCourse().equals(course)).toList();
                course.setCourseItems(items);
            }

            courseItemRepository.saveAll(courseItems);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void initData() {
        // create Users
        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder().username("admin").password(passwordEncoder.encode("admin")).email("email@gmail.com").firstName("Piotr").lastName("Pabich").bio("Passionate about technology, design, and the power of innovation. Always seeking new challenges and ways to make an impact.").role(Role.ADMIN).build());
        users.add(User.builder().username("user").password(passwordEncoder.encode("user")).email("user@gmail.com").firstName("Jan").lastName("Tomczyk").bio("Adventurous soul, chasing dreams one step at a time. Lover of art, nature, and good conversations. Here to make memories").role(Role.USER).build());
        users.add(User.builder().username("user1").password(passwordEncoder.encode("user1")).email("user1@gmail.com").firstName("Tomasz").lastName("Kukułka").bio("Avid reader, passionate writer, and eternal optimist. Finding beauty in the little things and spreading positivity wherever I go.").role(Role.USER).build());
        userRepository.saveAll(users);
        // create Courses
        ArrayList<Course> courses = new ArrayList<>();
        for(int i = 0; i < 21; i++) {
            String name = String.format("Most frequently used english words - Top %d%%", (99 - i));
            String description = "Comprehensive program designed to enhance individuals' English language skills by expanding their vocabulary with commonly known words.";
//            String description = "The \"Mastering Everyday English Vocabulary\" course is a comprehensive program designed to enhance individuals' English language skills by expanding their vocabulary with commonly known words, enabling effective communication and improved reading and writing abilities.";
            CourseLevel level = i < 5 ? CourseLevel.BEGINNER : i < 15 ? CourseLevel.INTERMEDIATE : CourseLevel.ADVANCED;
            courses.add(Course.builder().title(name).description(description).level(level).build());
        }
        courseRepository.saveAll(courses);
        loadCourseItemsFromJson(courses);
        ArrayList<UserCourse> userCourses = new ArrayList<>();
        for(User user: users) {
            new Random().ints(0, courses.size()).distinct().limit(5).forEach(value -> {
                Course course = courses.get(value);
                UserCourse userCourse = UserCourse.builder().completed(false)
                        .progress(0.0).user(user).course(course).build();
                List<UserCourseItem> userCourseItems = new ArrayList<>();
                for(CourseItem courseItem: course.getCourseItems()) {
                    userCourseItems.add(UserCourseItem.builder().courseItem(courseItem).isLearned(false).userCourse(userCourse).build());
                }
                userCourse.setUserCourseItems(userCourseItems);
                userCourses.add(userCourse);
            });
        }
        userCourseRepository.saveAll(userCourses);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initData();
    }
}
