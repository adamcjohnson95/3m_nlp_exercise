# 3m_nlp_exercise

Things to check before running the program:

1) If you grabbed it from here, you may have automatically imported the libraries for openNLP. If not, you can find all of these in the /lib/ folder of the project. Go to the project structure of the project and add all of the .jar files from that folder.

2) Inside of mySentenceDetector, on line 43, you may need up update the path to the sentence model inside of the FileInputStream depending on how your software pulled the files. (The easiest tricky I found was to print out a listing of the files in the "." path. That way you know where you need to build your path from.)

3) I did not upload the build configuration with this project. You can generate one easily (Use type "Application" and select the Main.java file as the main class.)
