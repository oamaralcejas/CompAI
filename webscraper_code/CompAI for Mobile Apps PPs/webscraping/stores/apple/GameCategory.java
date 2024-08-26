package lu.svv.saa.linklaters.privacypolicies.webscraping.stores.apple;

public class GameCategory {
    private static final String APPLE_STORE_GAME_ACTION="https://apps.apple.com/gb/charts/iphone/action-games/7001";
    private static final String APPLE_STORE_GAME_ADVENTURE="https://apps.apple.com/gb/charts/iphone/adventure-games/7002";
    private static final String APPLE_STORE_GAME_BOARD="https://apps.apple.com/gb/charts/iphone/board-games/7004";
    private static final String APPLE_STORE_GAME_CARD="https://apps.apple.com/gb/charts/iphone/card-games/7005";
    private static final String APPLE_STORE_GAME_CASINO="https://apps.apple.com/gb/charts/iphone/casino-games/7006";
    private static final String APPLE_STORE_GAME_CASUAL="https://apps.apple.com/gb/charts/iphone/casual-games/7003";
    private static final String APPLE_STORE_GAME_FAMILY="https://apps.apple.com/gb/charts/iphone/family-games/7009";
    private static final String APPLE_STORE_GAME_MUSIC="https://apps.apple.com/gb/charts/iphone/music-games/7011";
    private static final String APPLE_STORE_GAME_PUZZLE="https://apps.apple.com/gb/charts/iphone/puzzle-games/7012";
    private static final String APPLE_STORE_GAME_RACING="https://apps.apple.com/gb/charts/iphone/racing-games/7013";
    private static final String APPLE_STORE_GAME_ROLE="https://apps.apple.com/gb/charts/iphone/role-playing-games/7014";
    private static final String APPLE_STORE_GAME_SIMULATION="https://apps.apple.com/gb/charts/iphone/simulation-games/7015";
    private static final String APPLE_STORE_GAME_SPORTS="https://apps.apple.com/gb/charts/iphone/sports-games/7016";
    private static final String APPLE_STORE_GAME_STRATEGY="https://apps.apple.com/gb/charts/iphone/strategy-games/7017";
    private static final String APPLE_STORE_GAME_TRIVIA="https://apps.apple.com/gb/charts/iphone/trivia-games/7018";
    private static final String APPLE_STORE_GAME_WORD="https://apps.apple.com/gb/charts/iphone/word-games/7019";
    public static String getSubCategoryEndPoint(String subcategoryName) {
        switch (subcategoryName) {
            case "action":
                return APPLE_STORE_GAME_ACTION;
            case "adventure":
                return APPLE_STORE_GAME_ADVENTURE;
            case "board":
                return APPLE_STORE_GAME_BOARD;
            case "card":
                return APPLE_STORE_GAME_CARD;
            case "casino":
                return APPLE_STORE_GAME_CASINO;
            case "casual":
                return APPLE_STORE_GAME_CASUAL;
            case "family":
                return APPLE_STORE_GAME_FAMILY;
            case "music":
                return APPLE_STORE_GAME_MUSIC;
            case "puzzle":
                return APPLE_STORE_GAME_PUZZLE;
            case "racing":
                return APPLE_STORE_GAME_RACING;
            case "role":
                return APPLE_STORE_GAME_ROLE;
            case "simulation":
                return APPLE_STORE_GAME_SIMULATION;
            case "sports":
                return APPLE_STORE_GAME_SPORTS;
            case "strategy":
                return APPLE_STORE_GAME_STRATEGY;
            case "trivia":
                return APPLE_STORE_GAME_TRIVIA;
            case "word":
                return APPLE_STORE_GAME_WORD;
            default:
                return "";
        }
    }
}
