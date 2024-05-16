# SIT305-Task-8.2HD

SIT305 Task 8.2 HD

**Project name**: Food Rescue

**Student name**: Jizhi Zhang

**Student ID**: 219083324

**Project link**: https://github.com/JizhiZhang/SIT305-Task-8.2HD

**Demo video link:** https://www.youtube.com/watch?v=TVFeaqvK_lw



**Overview:**

Food Rescue is fighting one of the world's largest environmental issues - food waste. My mission is to cut food waste by making food more accessible and affordable. With the Food Rescue app, user can now rescue delicious food from restaurants, cafes and grocers at a reasonable price. Besides, Food Rescue allows users to detect food and get related information by their own mobile phone camera. It is also equipped with a chatbot to help users become familiar with and use Food Rescue faster.


**Product Purpose:**

The purpose of Food Rescue is to reduce food waste. Food Rescue allows users to add food items, including food pictures, name, description, price, quantity, and pick up time and location. Users can add several food items into cart and use PayPal to pay for them. In addition, Food Rescue can use TensorFlow Lite and OpenCV to perform object and food detection by applying for permission of use the user's mobile phone camera. At last, Food Rescue has a voice-enabled chatbot that can introduce the information and functions of this app through text and voice communication with users, which can help users become familiar with and use Food Rescue faster. These new features make the app more intelligent and user-friendly, which makes my project creative.


**Features:**
1. User account registration and login function.
2. Add food information items, including food image, name, description, price, date, pick up time, quantity and location.
3. Map function. Using Google Place SDK for Android and place autocomplete service, so that users can use Google Maps to search and add locations, and mark them on the map.
4. My List function. The food information entered by users can be displayed separately according to the account.
5. Payment function. According to the price and quantity of food, users can use PayPal to pay the corresponding amount.
6. Food details. After clicking each food item on the homepage, users can view the corresponding detailed food information.
7. Cart function. Users can add food items to the shopping cart and pay the total price.
8. Image classification function. The app can recognize various classes of images which means if users choose an image, it will tell them what it is.
9. Chatbot function. A voice-enabled Android Chatbot powered by IBM Watson which can respond to customer queries.
10. Real-time object detection function. Can identify objects and present information and their positions in real time.


**Design:**
 ![SIT305 Task 8 2HD Design Image](https://user-images.githubusercontent.com/69889275/121335597-51626600-c94d-11eb-8415-de309f13f65a.png)




**Advanced Concepts/Features:**

**1. Image classification.** 
Image classification function belongs to Android Machine Learning. An image classification model is trained to recognize various classes of images. TensorFlow Lite provides optimized pre-trained models that I deployed in the mobile application. In this app, when users add food items, they are asked to add food pictures. After adding a picture, the picture will be displayed in the picture box. By clicking the picture box, the app will perform image recognition and automatically fill the title of the food with the recognition result, which provides users with a more intelligent and humanized service.

**2. Chatbot.**
A voice-enabled Android Chatbot powered by IBM Watson. Use IBM Watson Assistant to create a chatbot. Define intents and entities and building a dialog flow for chatbot to respond to customer queries. Enable Speech to Text and Text to Speech services for easy interaction with the Android app. In the home page, the user can click the robot button in the lower left corner to start a conversation with chatbot. The content of the conversation includes app introduction, function description, app usage tutorial, etc., so as to help users master the app usage method more quickly, and make the app more intelligent and user-friendly.

**3. Real-time Object detection.** 
Real-time Object detection function belongs to Android Machine Learning. Real-time object detection function is implemented using TensorFlow Lite and OpenCV. The app uses the camera to detect objects by obtaining the camera permission of the mobile device. The app can identify objects and present information and their positions in real time, which can help users detect food and obtain related information.
