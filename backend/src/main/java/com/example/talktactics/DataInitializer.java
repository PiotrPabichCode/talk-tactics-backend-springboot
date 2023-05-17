package com.example.talktactics;

import com.example.talktactics.models.Answer;
import com.example.talktactics.models.Course;
import com.example.talktactics.models.Task;
import com.example.talktactics.models.User;
import com.example.talktactics.repositories.AnswerRepository;
import com.example.talktactics.repositories.CourseRepository;
import com.example.talktactics.repositories.TaskRepository;
import com.example.talktactics.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

@Component
@Transactional
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    public void initData() {
        // create Users
        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder().isAdmin(true).login("admin").password("admin").build());
        users.add(User.builder().isAdmin(false).login("test1").password("test1").build());
        users.add(User.builder().isAdmin(false).login("test2").password("test2").build());
        userRepository.saveAll(users);
        // create Course
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(Course.builder().name("Language Mastery: Advanced Communication Skills").description("Ten kurs został zaprojektowany dla osób, które chcą doskonalić swoje umiejętności komunikacyjne w języku angielskim na zaawansowanym poziomie. Skupia się na rozwijaniu płynności językowej, precyzji w wyrażaniu myśli oraz poszerzaniu słownictwa w różnorodnych kontekstach. Uczestnicy będą mieli okazję praktykować swobodną konwersację, debatować na różne tematy oraz doskonalić umiejętność zrozumienia ze słuchu poprzez autentyczne materiały audio i wideo.").level("Advanced").build());
        courses.add(Course.builder().name("Business English: Effective Communication in the Workplace").description("Ten kurs skierowany jest do profesjonalistów, którzy chcą doskonalić swoje umiejętności komunikacyjne w języku angielskim w kontekście biznesowym. Uczestnicy nauczą się skutecznie komunikować się w miejscu pracy, w tym prowadzić efektywne prezentacje, negocjować umowy, redagować profesjonalne e-maile i raporty. Kurs skupi się również na rozwoju umiejętności słuchania ze zrozumieniem oraz pisania precyzyjnych i zwięzłych tekstów biznesowych.").level("Intermediate").build());
        courses.add(Course.builder().name("English for Travelers: Practical Language Skills for Globetrotters").description("Ten kurs został stworzony dla osób, które planują podróżować i chcą nauczyć się praktycznych umiejętności językowych w kontekście podróży. Uczestnicy nauczą się podstawowych zwrotów, które są niezbędne do porozumiewania się w sytuacjach podróżnych, takich jak rezerwacja hotelu, zamawianie jedzenia w restauracji, poruszanie się po mieście czy zakupy. Kurs będzie również obejmować praktyczne wskazówki dotyczące radzenia sobie w różnych sytuacjach kulturowych, jak również podstawy rozumienia ogólnodostępnych informacji, takich jak rozkłady jazdy czy oznaczenia na lotniskach.").level("Beginner").build());
        courseRepository.saveAll(courses);

        // create tasks
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(Task.builder()
                .name("Vocabulary Expansion")
                .word("resilient")
                .partOfSpeech("Adjective")
                .description("Use a single word to portray someone's ability to recover quickly from challenges or setbacks.")
                .course(courses.get(0))
                .build());
        tasks.add(Task.builder()
                .name("Idiomatic Expressions")
                .word("hit the nail on the head")
                .partOfSpeech("Phrase")
                .description("Explain the meaning of a popular phrase that signifies a precise and accurate statement.")
                .course(courses.get(0))
                .build());
        tasks.add(Task.builder()
                .name("Listening Comprehension")
                .word("elicit")
                .partOfSpeech("Verb")
                .description("Identify a verb that means to extract or draw out information from a conversation.")
                .course(courses.get(0))
                .build());
        tasks.add(Task.builder()
                .name("Oral Presentation")
                .word("persuasive")
                .partOfSpeech("Adjective")
                .description("Deliver a speech with the intention of persuading the audience by presenting logical arguments and compelling evidence.")
                .course(courses.get(0))
                .build());
        tasks.add(Task.builder()
                .name("Debate")
                .word("contradict")
                .partOfSpeech("Verb")
                .description("Engage in a discussion where you challenge or refute the arguments presented by your opponent.")
                .course(courses.get(0))
                .build());

        tasks.add(Task.builder()
                .name("Email Writing")
                .word("concise")
                .partOfSpeech("Adjective")
                .description("Compose an email that effectively conveys complex information in a concise and clear manner.")
                .course(courses.get(1))
                .build());

        tasks.add(Task.builder()
                .name("Negotiation Scenario")
                .word("compromise")
                .partOfSpeech("Noun/Verb")
                .description("Role-play a negotiation situation, aiming to find mutually acceptable solutions by making concessions.")
                .course(courses.get(1))
                .build());

        tasks.add(Task.builder()
                .name("Presentation Skills")
                .word("engage")
                .partOfSpeech("Verb")
                .description("Demonstrate the ability to captivate and involve the audience during a presentation on a chosen topic.")
                .course(courses.get(1))
                .build());

        tasks.add(Task.builder()
                .name("Meeting Participation")
                .word("collaborate")
                .partOfSpeech("Verb")
                .description("Collaborate and contribute actively in a simulated business meeting to foster teamwork and achieve shared goals.")
                .course(courses.get(1))
                .build());

        tasks.add(Task.builder()
                .name("Telephone Conversation")
                .word("clarify")
                .partOfSpeech("Verb")
                .description("Engage in a phone conversation with a client or customer, seeking to ensure mutual understanding through requesting or providing additional information.")
                .course(courses.get(1))
                .build());

        tasks.add(Task.builder()
                .name("Ordering Food")
                .word("appetizer")
                .partOfSpeech("Noun")
                .description("Role-play a scenario in a restaurant where you request a specific dish to commence your meal.")
                .course(courses.get(2))
                .build());

        tasks.add(Task.builder()
                .name("Asking for Directions")
                .word("landmark")
                .partOfSpeech("Noun")
                .description("Approach someone and seek guidance to a particular location by referencing recognizable landmarks.")
                .course(courses.get(2))
                .build());

        tasks.add(Task.builder()
                .name("Reading Signs")
                .word("exit")
                .partOfSpeech("Noun")
                .description("Analyze and interpret signs commonly found in public spaces to identify symbols or directions guiding travelers.")
                .course(courses.get(2))
                .build());

        tasks.add(Task.builder()
                .name("Shopping Interaction")
                .word("bargain")
                .partOfSpeech("Noun/Verb")
                .description("Engage in a role-play where you discuss prices or negotiate a better deal with a shopkeeper.")
                .course(courses.get(2))
                .build());

        tasks.add(Task.builder()
                .name("Transportation Vocabulary")
                .word("fare")
                .partOfSpeech("Noun")
                .description("Compile a list of words related to transportation, including terms associated with ticket purchase or payment.")
                .course(courses.get(2))
                .build());
        taskRepository.saveAll(tasks);

        // Create answers
        ArrayList<Answer> answers = new ArrayList<>();
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss");

        for (int i = 0; i < 10; i++) {
            Task randomTask = tasks.get(random.nextInt(tasks.size()));
            User randomUser = users.get(random.nextInt(users.size()));

            LocalDateTime currentTime = LocalDateTime.now();
            String formattedFinishTime = currentTime.format(formatter);

            Answer randomAnswer = Answer.builder()
                    .content("Random answer content " + i)
                    .finishTime(LocalDateTime.parse(formattedFinishTime, formatter))
                    .task(randomTask)
                    .user(randomUser)
                    .build();

            answers.add(randomAnswer);
        }
        answerRepository.saveAll(answers);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initData();
    }
}
