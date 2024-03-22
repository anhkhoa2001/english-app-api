package org.base.utils;

public class Constants {

    public interface ROLE_CODE {
        String STUDENT = "STUDENT";

        String PROFESSOR = "PROFESSOR";

        String ADMIN = "ADMIN";
    }

    public interface FORMAT_TIME {
        String DATE_TIME = "HH:mm:ss dd/MM/yyyy";

        String TIME = "dd/MM/yyyy";
    }

    public interface TYPE_LOGIN {
        String GOOGLE = "GOOGLE";
        String GITHUB = "GITHUB";

        String FACEBOOK = "FACEBOOK";
    }

    public static final String TOKEN_TYPE = "Bearer ";

    public static final String TEMPLE_SPLIT = "|!@#$%^&*()|";

    public interface TYPE_QUESTION {
        String CONTENT_TEXT = "content_text";

        String NO_CONTENT = "no_content";

        String ANS_TEXT = "ans_text";

        String MULTI_CHOICE = "multi_choice";

        String AUDIO = "audio";
    }

    public interface TYPE_QUESTION_ITEM {
        String SINGLE = "Single";

        String GROUP = "Group";
    }

    public interface SERVICE {

        interface  USER {
            String NAME = "user";
            String TO = "topic-user-to";

            String FROM = "topic-user-from";
        }

        interface  DOCHUB {
            String NAME = "dochub";
            String TO = "topic-dochub-to";

            String FROM = "topic-dochub-from";
        }

        interface  CONVERSATION {
            String NAME = "conversation";
            String TO = "topic-conversation-to";

            String FROM = "topic-conversation-from";
        }

        interface  COURSE {
            String NAME = "course";
            String TO = "topic-course-to";

            String FROM = "topic-course-from";
        }

        interface  BLOG {
            String NAME = "blog";
            String TO = "topic-blog-to";

            String FROM = "topic-blog-from";
        }

        interface  EXAM {
            String NAME = "exam";
            String TO = "topic-exam-to";

            String FROM = "topic-exam-from";
        }
    }


}
