#Readme for Debate (AKA tristanhoobroeckx-pset6)

![Screenshot 1](https://github.com/Tristanhx/tristanhoobroeckx-pset6/blob/master/doc/DebateScreenshot1.jpg?raw=true)
![Screenshot 2](https://github.com/Tristanhx/tristanhoobroeckx-pset6/blob/master/doc/DebateScreenshot2.jpg?raw=true)
![Screenshot 3](https://github.com/Tristanhx/tristanhoobroeckx-pset6/blob/master/doc/DebateScreenshot3.jpg?raw=true)
![Screenshot 4](https://github.com/Tristanhx/tristanhoobroeckx-pset6/blob/master/doc/DebateScreenshot4.jpg?raw=true)
![Screenshot 5](https://github.com/Tristanhx/tristanhoobroeckx-pset6/blob/master/doc/DebateScreenshot5.jpg?raw=true)
![Screenshot 6](https://github.com/Tristanhx/tristanhoobroeckx-pset6/blob/master/doc/DebateScreenshot6.jpg?raw=true)

This is intended as a Debate app. In the first screen you either sign up of login and in either case choose your displayname. If you don't choose a displayname, it will be set to 'Human'. Passwords must be six digits or longer. In the next screen you can choose your team - Red or Blue. Your choice will determine how your displayname will be prefixed. You can always go back and switch teams later. On this screen is also a logout button present. The third screen has a message display and an input component were you can type your messages. This component expands as you add more lines and pushes the message display upwards. Also the input component and send button move with the keyboard. The send button is either red or blue depending on your team. Messages update instantly. The app uses Firebase to store messages and accounts.

Known bugs:
There seems to be a bug in Firebase, concerning updating the Displayname. It only updates after logout and next login. So I force it to logout and login when the user creates a new account or logs in after changing the displayname. This solves the problem somewhat, but sometimes it still updates too late. I even make it wait for two seconds between logging out and in. Still this bug sometimes persists. A reinstall usually fixes the problem.

[![BCH compliance](https://bettercodehub.com/edge/badge/Tristanhx/tristanhoobroeckx-pset6?branch=master)](https://bettercodehub.com/)

