# BPM Finder

## A Software that finds BPM of songs with some practical applications

### What Will This Application Do?
This is a program is able to identify the bpm (beat per minute) of songs the user input and sort a playlist of music accordingly.
It will include some applications for the bpm. Although these features are not guaranteed and may not be applied in 
later phases. 
They include:

**Exercise Features**
- A bpm detector for exercising from user input. The user can tap out their bpm for activities such as running, walking,
swimming, skiing, weight lifting etc.
  - The application can find list of songs with the same bpm as user's activity. When the user listens to this filtered
  playlist, the music will match the pace of running, walking, or whatever activity the user is doing.
- A pace setter
  - If the user wants to walk a certain distance in a certain timeframe. With some user input such their stride length.
The application can find songs that set the correct pace for the user.
  
**Music and Editing Features**
- Application can match a song with a beat from a timestamp of a video

### Who will use it?
This application will be usable for many people including those who exercise, music producers, video editors, etc.

### Why is this project of interest to you?
I want to do a music related project since I am heavily inspired by many music artists. Originally for this project I was
considering simply making a music streaming platform. However, that project is too simple. Since some of my favourite music
artists are producers, I want to experience what they do and get involved with the backbone of creating music like the bpm.
I also found multiple practical uses for this project and as someone who runs and exercises a lot. I personally believe 
having a music pace setter will be very practical. 

### User Stories
- As a user, I want to be able to insert wav files into a playlist of music
- As a user, I want to be able to view my playlist and shuffle the playlist
- As a user, I want to be able to filter the bpm of music files I input (perhaps initially bpm input can be done manually)
- As a user, I want to be able to manually input a beat and get filtered songs with the same bpm
- As a user, I want to be able save my filtered/shuffled/manipulated playlist
- As a user, I want to be able save the music I uploaded
- As a user, I want to be able loaded my previously saved filtered/shuffled/manipulated playlist
- As a user, I want to be able load my previously saved music I uploaded

### Instructions for Grader
1. Upon running the program, there will be a small minimized window that appear, you will need to open it up 
   to fully see the application.
2. In order to add Songs to the playlist, click on Upload Song from the main menu,
   there will be instructions on how to upload music into the playlist, keep in mind that the file must exist inside
   the data/music folder, and that the title must be the same as the name of the file.
3. For the two related action, you can click into view songs from the main menu and any action on the control panel 
   at the bottom of the window is a related action. Keep in mind these actions will not change the master playlist of 
   all songs uploade, instead they are copies of playlists you create. Actions include:
    - Adding a song from those you uploaded to your filtered/shuffled/manipulated playlist
    - Deleting song from your current playlist 
    - Shuffling the order of your playlist
    - Play all the songs in order of your correct playlist
    - Filter the current playlist by a filter
    - Saving a local manipulated playlist
    - Loading a local manipulated playlist that was previously stored
4. To generate the second requirement there are two seperate sections of save/load 
   - as mentioned above, local playlists can be saved and load that way
   (when local playlists are saved, the masterplaylist of all songs is also automatically saved)
   - The master playlist of all songs you uploaded can also be saved and loaded (with the condition that the song files 
   uploaded into the computer is undisturbed). This is achieved by the save and load button on the main menu screen
     (first screen)
5. Visual component included is the spinning record on the right of the View Songs page accessed from the main menu.