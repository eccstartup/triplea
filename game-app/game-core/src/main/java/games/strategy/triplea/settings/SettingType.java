package games.strategy.triplea.settings;

/**
 * Groups settings together. Each {@link GameSettingUiBinding} will map to one {@code SettingType}.
 * Each {@code SettingType} will have its own tab in the SettingsWindow, every ui binding with that
 * type will be shown on that tab.
 */
public enum SettingType {
  AI("AI"),

  BATTLE_SIMULATOR("战斗模拟器"),

  COMBAT("战斗"),

  GAME("游戏"),

  MAP_SCROLLING("地图滚动"),

  NETWORK("网络"),

  LOOK_AND_FEEL("界面主题"),

  FOLDER_LOCATIONS("文件夹"),

  TESTING("测试"),

  PLAY_BY_FORUM_EMAIL("论坛/邮件对战");

  final String tabTitle;

  SettingType(final String tabTitle) {
    this.tabTitle = tabTitle;
  }
}
