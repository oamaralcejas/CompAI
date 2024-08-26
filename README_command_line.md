# Compai - Command Line version

This project aims to run Compai from command line by analyzing privacy policies from a folder or an online store.

## Getting started

The entry point is the class lu/svv/saa/linklaters/privacypolicies/Main.java. Compile the code and then pass as parameter the flag -s and the path to the JSON file which contains the settings of the experiment you want to run.

Example:

```
java lu/svv/saa/linklaters/privacypolicies/Main.java -s <path_to_your_settings_JSON_file>
```
The settings_examples folder contains examples of JSON files for different scenarios:
- setting.json runs the tool over a folder.
- setting_google.json runs the tool by downloading the privacy policies from the Google Play Store.
- setting_apple.json runs the tool by downloading the privacy policies from the Apple Store.

The setting JSON file is composed of pairs of key:value following the JSON syntax.

The mandatory pairs are:
- "input-folder": "path-value", the path where the privacy policies are stored.
- "output-folder": "path-value", the path where you want to store the output of the process.
- "output-format": "format", the output format of the process: only the JSON is supported at the moment.
- "default-questionnaire": questionnaire, the default answers to the questionnaire for the experiment. See the section [Questionnaire](#Questionnaire) on how to define the questionnaire section.

The optional pairs are:
- "questionnaire-folder": "path-value", the path where the questionnaire JSON files are stored
- "store": store, the store from where get the privacy policies. See the section [Store](#Store)

The order of the pairs doesn't matter.

The questionnaire JSON files in the questionnaire folder, have to be named as the name of the privacy policy are referring to.
Example
```
Hikary.docx -> <path_to_questionnaire_folder>/Hikary.json
```
If the questionnaire-folder key is not defined or the program doesn't find the questionnaire JSON file in the folder, the program will use the default questionnaire.
If the questionnaire provided by the user doesn't contain a key:value, the default key:value in the default questionnaire will be used.

If the store key:value is not defined, the tool will retrieve the privacy policies directly from the input folder.

## Questionnaire

The questionnaire JSON file is defined as the following:

```
{
    "controller-name":"controller name",
    "controller-representative-name":"controller representative name",
    "controller-in-ea": "value", //value can be Yes or No
    "dpo": "number", //it can be any number from 1 to 3 (separate by comma if combined) or only 4
    "other-recipients": "value", //value can be Yes or No 
    "how-to-collect-data": "value",  //value can be Direct, Indirect or Both 
    "transfer-of-personal-data": "value", //value can be Yes or No
    "places-list": "value" //if the value is Outside European Economic Area, the controller representative name will be checked
}
```
Remember to name the file as the name of the privacy policy you want to analyze otherwise the tool will consider the default questionnaire.


The structure of the default-questionnaire key in the setting JSON file is the following:

```
"default-questionnaire": {
    "controller-name":"controller name",
    "controller-representative-name":"controller representative name",
    "controller-in-ea": "value", //value can be Yes or No
    "dpo": "value", //it can be any number from 1 to 3 (separate by comma if combined) or only 4
    "other-recipients": "value", //value can be Yes or No 
    "how-to-collect-data": "value",  //value can be Direct, Indirect or Both 
    "transfer-of-personal-data": "value", //value can be Yes or No
    "places-list": "value" //if the value is Outside European Economic Area, the controller representative name will be checked
}
```

## Store

The store key:value has the following structure:

```
  "store": {
    "name": "value", //it can be apple-store or google-play-store
    "category": "categoryName-subCategory" //set only in case of apple-store
  }
```
The mandatory pair is "name": "value" which is the name of the store.

The optional pair is "category": "categoryName-subCategory".
If category is missing the default endpoint will be used.
The values for the default endpoints are:
- Apple Store in the class lu/svv/saa/linklaters/privacypolicies/webscraping/stores/apple/AppleStore.java in the variable APPLE_STORE_ENDPOINT
- Google Play Store in the class lu/svv/saa/linklaters/privacypolicies/webscraping/stores/google/GooglePlayStore.java in the variable GOOGLE_PLAY_STORE_ENDPOINT

If it is set, the web scraping will look for privacy policies of the specified category.
This works at the moment only for the Apple Store. Don't define this value for the Google Play Store.

The category value is a string categoryName-subCategory.
The acceptable values for categoryName are:
- app: for the applications
- games: for the games

The list of subCategory is in the method getSubCategoryEndPoint contained in the class:
- lu/svv/saa/linklaters/privacypolicies/webscraping/stores/apple/AppCategory.java for category name "app"
- lu/svv/saa/linklaters/privacypolicies/webscraping/stores/apple/GameCategory.java for category name "games"

Examples:
```
"store": {
    "name": "apple-store",
    "category": "games-adventure"
  }
```
This  will use the Apple Store to download the privacy policies under the category games with the genre adventure.

```
"store": {
    "name": "apple-store",
    "category": "app-developer"
  }
```
This  will use the Apple Store to download the privacy policies under the category app for developing.

```
  "store": {
    "name": "google-play-store"
  }
```
This will use the Google Play Store to download the privacy policies from the default endpoint.
