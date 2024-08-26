package lu.svv.saa.linklaters.privacypolicies.webscraping.stores.apple;

public class AppCategory {
    private static final String APPLE_STORE_BOOK="https://apps.apple.com/gb/charts/iphone/books-apps/6018";
    private static final String APPLE_STORE_BUSINESS="https://apps.apple.com/gb/charts/iphone/business-apps/6000";
    private static final String APPLE_STORE_DEVELOPER="https://apps.apple.com/gb/charts/iphone/developer-tools-apps/6026";
    private static final String APPLE_STORE_EDUCATION="https://apps.apple.com/gb/charts/iphone/education-apps/6017";
    private static final String APPLE_STORE_ENTERTAINMENT="https://apps.apple.com/gb/charts/iphone/entertainment-apps/6016";
    private static final String APPLE_STORE_FINANCE="https://apps.apple.com/gb/charts/iphone/finance-apps/6015";
    private static final String APPLE_STORE_FOOD="https://apps.apple.com/gb/charts/iphone/food-drink-apps/6023";
    private static final String APPLE_STORE_GRAPHICS="https://apps.apple.com/gb/charts/iphone/graphics-design-apps/6027";
    private static final String APPLE_STORE_HEALTH="https://apps.apple.com/gb/charts/iphone/health-fitness-apps/6013";
    private static final String APPLE_STORE_KIDS="https://apps.apple.com/gb/charts/iphone/kids-apps/36?ageId=0";
    private static final String APPLE_STORE_LIFESTYLE="https://apps.apple.com/gb/charts/iphone/lifestyle-apps/6012";
    private static final String APPLE_STORE_MAGAZINES="https://apps.apple.com/gb/charts/iphone/magazines-newspapers-apps/6021";
    private static final String APPLE_STORE_MEDICAL="https://apps.apple.com/gb/charts/iphone/medical-apps/6020";
    private static final String APPLE_STORE_MUSIC="https://apps.apple.com/gb/charts/iphone/music-apps/6011";
    private static final String APPLE_STORE_NAVIGATION="https://apps.apple.com/gb/charts/iphone/navigation-apps/6010";
    private static final String APPLE_STORE_NEWS="https://apps.apple.com/gb/charts/iphone/news-apps/6009";
    private static final String APPLE_STORE_PHOTO="https://apps.apple.com/gb/charts/iphone/photo-video-apps/6008";
    private static final String APPLE_STORE_PRODUCTIVITY="https://apps.apple.com/gb/charts/iphone/productivity-apps/6007";
    private static final String APPLE_STORE_REFERENCE="https://apps.apple.com/gb/charts/iphone/reference-apps/6006";
    private static final String APPLE_STORE_SHOPPING="https://apps.apple.com/gb/charts/iphone/shopping-apps/6024";
    private static final String APPLE_STORE_SOCIAL="https://apps.apple.com/gb/charts/iphone/social-networking-apps/6005";
    private static final String APPLE_STORE_SPORTS="https://apps.apple.com/gb/charts/iphone/sports-apps/6004";
    private static final String APPLE_STORE_TRAVEL="https://apps.apple.com/gb/charts/iphone/travel-apps/6003";
    private static final String APPLE_STORE_UTILITIES="https://apps.apple.com/gb/charts/iphone/utilities-apps/6002";
    private static final String APPLE_STORE_WEATHER="https://apps.apple.com/gb/charts/iphone/weather-apps/6001";

    public static String getSubCategoryEndPoint(String subcategoryName){
        switch (subcategoryName) {
            case "book":
                return APPLE_STORE_BOOK;
            case "business":
                return APPLE_STORE_BUSINESS;
            case "developer":
                return APPLE_STORE_DEVELOPER;
            case "education":
                return APPLE_STORE_EDUCATION;
            case "entertainment":
                return APPLE_STORE_ENTERTAINMENT;
            case "finance":
                return APPLE_STORE_FINANCE;
            case "food":
                return APPLE_STORE_FOOD;
            case "graphics":
                return APPLE_STORE_GRAPHICS;
            case "health":
                return APPLE_STORE_HEALTH;
            case "kids":
                return APPLE_STORE_KIDS;
            case "lifestyle":
                return APPLE_STORE_LIFESTYLE;
            case "magazines":
                return APPLE_STORE_MAGAZINES;
            case "medical":
                return APPLE_STORE_MEDICAL;
            case "music":
                return APPLE_STORE_MUSIC;
            case "navigation":
                return APPLE_STORE_NAVIGATION;
            case "news":
                return APPLE_STORE_NEWS;
            case "photo":
                return APPLE_STORE_PHOTO;
            case "productivity":
                return APPLE_STORE_PRODUCTIVITY;
            case "reference":
                return APPLE_STORE_REFERENCE;
            case "shopping":
                return APPLE_STORE_SHOPPING;
            case "social":
                return APPLE_STORE_SOCIAL;
            case "sports":
                return APPLE_STORE_SPORTS;
            case "travel":
                return APPLE_STORE_TRAVEL;
            case "utilities":
                return APPLE_STORE_UTILITIES;
            case "weather":
                return APPLE_STORE_WEATHER;
            default:
                return "";
        }

    }
}
