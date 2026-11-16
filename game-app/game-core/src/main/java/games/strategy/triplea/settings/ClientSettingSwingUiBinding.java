package games.strategy.triplea.settings;

import static games.strategy.triplea.settings.SelectionComponentFactory.booleanRadioButtons;
import static games.strategy.triplea.settings.SelectionComponentFactory.diceRollerOverrideSelection;
import static games.strategy.triplea.settings.SelectionComponentFactory.folderPath;
import static games.strategy.triplea.settings.SelectionComponentFactory.intValueRange;
import static games.strategy.triplea.settings.SelectionComponentFactory.proxySettings;
import static games.strategy.triplea.settings.SelectionComponentFactory.selectionBox;

import games.strategy.engine.framework.lookandfeel.LookAndFeel;
import games.strategy.engine.framework.lookandfeel.RelationshipColorScheme;
import games.strategy.triplea.UrlConstants;
import games.strategy.triplea.settings.lobby.LobbySelectionViewFactory;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.swing.JComponent;
import javax.swing.UIManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Binds a {@code ClientSetting} to a UI component. This is done by adding an enum element. As part
 * of that the corresponding UI component, a {@code SelectionComponent} is specified. This then
 * automatically adds the setting to the settings window.
 *
 * <p>UI component construction is delegated to {@code SelectionComponentFactory}.
 *
 * <p>There is a 1:n between {@code ClientSettingUiBinding} and {@code ClientSetting}, though
 * typically it will be 1:1, and not all {@code ClientSettings} will be available in the UI.
 */
@AllArgsConstructor
enum ClientSettingSwingUiBinding implements GameSettingUiBinding<JComponent> {
  AI_MOVE_PAUSE_DURATION_BINDING("AI 移动暂停时长", SettingType.AI, "AI 各次移动之间的间隔（毫秒）") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.aiMovePauseDuration, 0, 3000);
    }
  },

  AI_COMBAT_STEP_PAUSE_DURATION_BINDING("AI 战斗步骤暂停时长", SettingType.AI, "AI 各战斗步骤之间的间隔（毫秒）") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.aiCombatStepPauseDuration, 0, 3000);
    }
  },

  ARROW_KEY_SCROLL_SPEED_BINDING("方向键滚动速度", SettingType.MAP_SCROLLING, "使用方向键时地图滚动的速度（像素）") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.arrowKeyScrollSpeed, 0, 500);
    }
  },

  MAP_ZOOM_FACTOR_BINDING("地图缩放系数", SettingType.MAP_SCROLLING, "使用滚轮或缩放键时地图缩放的速度") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.mapZoomFactor, 1, 100);
    }
  },

  BATTLE_CALC_SIMULATION_COUNT_DICE_BINDING(
      "模拟次数（骰子）", SettingType.BATTLE_SIMULATOR, "骰子游戏中默认的战斗模拟次数") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.battleCalcSimulationCountDice, 10, 100_000);
    }
  },

  BATTLE_CALC_SIMULATION_COUNT_LOW_LUCK_BINDING(
      "模拟次数（低运气）", SettingType.BATTLE_SIMULATOR, "低运气游戏中默认的战斗模拟次数") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.battleCalcSimulationCountLowLuck, 10, 100_000);
    }
  },

  CONFIRM_DEFENSIVE_ROLLS_BINDING("确认防御掷骰", SettingType.COMBAT, "防御时是否要等你确认所掷骰子后战斗才继续") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.confirmDefensiveRolls);
    }
  },

  CONFIRM_ENEMY_CASUALTIES_BINDING("确认敌方伤亡", SettingType.COMBAT, "是否只有在每位玩家 " + "都确认所选伤亡后战斗才继续") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.confirmEnemyCasualties);
    }
  },

  DICE_ROLLER_URI("骰子服务器地址", SettingType.TESTING, "用于 PBEM 和 PBF 游戏的骰子服务器") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return diceRollerOverrideSelection();
    }
  },

  SPACE_BAR_CONFIRMS_CASUALTIES_BINDING(
      "空格键确认伤亡",
      SettingType.COMBAT,
"""
When set to true casualty confirmation can be accepted by pressing space bar.
When set to false, the confirm casualty button has to always be clicked.""") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.spaceBarConfirmsCasualties);
    }
  },

  SHOW_AA_FLYOVER_WARNING("防空炮飞越警告", SettingType.COMBAT, "移动经过有防空炮防御的领地时，提示防空炮开火") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.showAaFlyoverWarning);
    }
  },

  SHOW_POTENTIAL_SCRAMBLE_WARNING("紧急起飞警告", SettingType.COMBAT, "攻击领地时，提示敌方可能紧急起飞战机") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.showPotentialScrambleWarning);
    }
  },

  LOOK_AND_FEEL_PREF_BINDING(
      "界面外观",
      SettingType.LOOK_AND_FEEL,
"""
Updates UI theme for TripleA.
WARNING: restart all running TripleA instances after changing this setting to avoid system instability.""") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      final Collection<UIManager.LookAndFeelInfo> lookAndFeels =
          LookAndFeel.getAvailableLookAndFeels();
      return selectionBox(
          ClientSetting.lookAndFeel,
          UIManager.LookAndFeelInfo.class,
          lookAndFeels,
          lookAndFeelClassName ->
              lookAndFeels.stream()
                  .filter(lookAndFeel -> lookAndFeel.getClassName().equals(lookAndFeelClassName))
                  .findAny(),
          UIManager.LookAndFeelInfo::getClassName,
          UIManager.LookAndFeelInfo::getName);
    }
  },

  RELATIONSHIP_COLOR_SCHEME_BINDING(
      "关系颜色",
      SettingType.LOOK_AND_FEEL,
"""
War/allied/neutral colors in the politics panel and status bar.
Color blind friendly uses orange/blue instead of red/green.""") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return selectionBox(
          ClientSetting.relationshipColorScheme,
          RelationshipColorScheme.class,
          List.of(RelationshipColorScheme.values()),
          Optional::of,
          Function.identity(),
          RelationshipColorScheme::getDisplayName);
    }
  },

  UI_SCALE_PERCENT_BINDING(
      "界面字体与尺寸缩放 (%)",
      SettingType.LOOK_AND_FEEL, "按此百分比缩放整个界面——包括字体和组件尺寸。" + "100 为默认大小；增大此值可获得更大、更易读的文字。") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.uiScalePercent, 75, 250);
    }
  },

  MAP_EDGE_SCROLL_SPEED_BINDING("地图滚动速度", SettingType.MAP_SCROLLING, "鼠标靠近地图边缘时地图滚动的速度（像素）") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.mapEdgeScrollSpeed, 0, 300);
    }
  },

  MAP_EDGE_SCROLL_ZONE_SIZE_BINDING(
      "滚动区域大小", SettingType.MAP_SCROLLING, "鼠标需要距地图边缘多近（像素）" + "，地图才会开始滚动") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.mapEdgeScrollZoneSize, 0, 300);
    }
  },

  NOTIFY_ALL_UNITS_MOVED("所有单位移动后通知", SettingType.GAME, "所有单位移动完毕后，游戏将显示弹出通知") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.notifyAllUnitsMoved);
    }
  },

  PROXY_CHOICE(
      "网络代理",
      SettingType.NETWORK,
"""
Configure TripleA's Network and Proxy Settings
This only effects Play-By-Forum games, dice servers, and map downloads.""") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return proxySettings(
          ClientSetting.proxyChoice, ClientSetting.proxyHost, ClientSetting.proxyPort);
    }
  },

  SERVER_START_GAME_SYNC_WAIT_TIME_BINDING(
      "开始游戏超时", SettingType.NETWORK, "游戏开始时等待所有客户端同步数据的最长时间（秒）") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.serverStartGameSyncWaitTime, 120, 1500);
    }
  },

  SERVER_OBSERVER_JOIN_WAIT_TIME_BINDING("观察者加入超时", SettingType.NETWORK, "主机等待客户端和观察者的最长时间（秒）") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.serverObserverJoinWaitTime, 60, 1500);
    }
  },

  SHOW_BATTLES_WHEN_OBSERVING_BINDING("以观察者身份显示战斗", SettingType.GAME, "仅作为观察者时是否显示战斗。") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.showBattlesWhenObserving);
    }
  },

  SHOW_BETA_FEATURES_BINDING(
      "显示测试版功能",
      SettingType.TESTING,
"""
Toggles whether to show 'beta' features. These are game features that are still under development and potentially may not be working yet.
Restart to fully activate""") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.showBetaFeatures);
    }
  },

  SHOW_SERIALIZE_FEATURES_BINDING(
      "使用新序列化方式",
      SettingType.TESTING,
"""
Toggles whether to use the new serialization mechanisms. This mechanism is still under development and potentially may break saved games and network games.
All players in the same game must have it set to the same value. Restart to fully activate""") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.showSerializeFeatures);
    }
  },

  USE_WEBSOCKET_NETWORK("使用 Websocket 网络（测试版）", SettingType.TESTING, "切换是否使用开发中的 websocket 网络") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.useWebsocketNetwork);
    }
  },

  USE_BOUNDED_CONTEXT_BATTLE_CALC(
      "使用限定上下文的战斗计算器（测试版）",
      SettingType.TESTING,
      "切换开发中的无引擎战斗胜率计算器。默认关闭；仍在 " + "开发中。下次打开战斗计算器时生效。") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.useBoundedContextBattleCalc);
    }
  },

  DO_NOT_REDRAW_MAP("不重绘地图", SettingType.TESTING, "切换发现科技时是否完全重绘地图。默认完全重绘；不重绘可使获得科技时的闪光效果消失。") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.doNotRedrawMap);
    }
  },

  LOBBY_URI_OVERRIDE_BINDING("大厅地址覆盖", SettingType.TESTING, "覆盖大厅服务器地址") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return LobbySelectionViewFactory.build();
    }
  },

  SAVE_GAMES_FOLDER_PATH_BINDING("存档文件夹", SettingType.FOLDER_LOCATIONS, "默认存放游戏存档的文件夹") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return folderPath(ClientSetting.saveGamesFolderPath);
    }
  },

  USER_MAPS_FOLDER_PATH_BINDING("地图文件夹", SettingType.FOLDER_LOCATIONS, "游戏引擎下载和查找地图文件的文件夹。") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return folderPath(ClientSetting.mapFolderOverride);
    }
  },

  WHEEL_SCROLL_AMOUNT_BINDING("鼠标滚轮滚动速度", SettingType.MAP_SCROLLING, "使用鼠标滚轮时地图滚动的速度（像素）") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return intValueRange(ClientSetting.wheelScrollAmount, 10, 300);
    }
  },

  EMAIL_SETTINGS("邮件对战偏好设置", SettingType.PLAY_BY_FORUM_EMAIL, "配置你偏好的邮件服务器设置。") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return SelectionComponentFactory.emailSettings(
          ClientSetting.emailServerHost,
          ClientSetting.emailServerPort,
          ClientSetting.emailServerSecurity,
          ClientSetting.emailUsername,
          ClientSetting.emailPassword);
    }
  },

  TRIPLEA_FORUM_SETTINGS("TripleA 论坛设置", SettingType.PLAY_BY_FORUM_EMAIL, "配置 TripleA 论坛的登录凭据") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return SelectionComponentFactory.forumPosterSettings(
          UrlConstants.TRIPLEA_FORUM,
          ClientSetting.tripleaForumUserId,
          ClientSetting.tripleaForumUsername,
          ClientSetting.tripleaForumToken);
    }
  },

  AXIS_AND_ALLIES_FORUM_SETTINGS(
      "Axis & Allies 论坛设置", SettingType.PLAY_BY_FORUM_EMAIL, "配置 Axis & Allies 论坛的登录凭据") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return SelectionComponentFactory.forumPosterSettings(
          UrlConstants.AXIS_AND_ALLIES_FORUM,
          ClientSetting.aaForumUserId,
          ClientSetting.aaForumUsername,
          ClientSetting.aaForumToken);
    }
  },

  UNIT_SCROLLER_HIGHLIGHT_TERRITORY("切换单位时高亮领地", SettingType.GAME, "切换单位时是否同时高亮所在领地") {
    @Override
    public SelectionComponent<JComponent> newSelectionComponent() {
      return booleanRadioButtons(ClientSetting.unitScrollerHighlightTerritory);
    }
  };

  @Getter(onMethod_ = {@Override})
  private final String title;

  @Getter(onMethod_ = {@Override})
  private final SettingType type;

  @Getter private final String description;
}
