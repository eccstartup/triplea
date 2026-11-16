package games.strategy.triplea.ui;

import java.util.Map;

/**
 * Maps map-data driven display names (player names, technologies, game steps and alliances) to
 * Simplified Chinese for on-screen display only.
 *
 * <p>These names originate from map XML and are used as identifiers in saves, network messages and
 * rule lookups. This class is deliberately a <em>display-only</em> layer: it never changes the
 * underlying name, only what is shown to the player. Unknown names are returned unchanged so that
 * custom maps and other languages keep working.
 */
public final class DisplayNameLocalizer {
  private DisplayNameLocalizer() {}

  private static final Map<String, String> PLAYER_NAMES =
      Map.ofEntries(
          Map.entry("Germans", "德国"),
          Map.entry("AI_Germans", "德国（AI）"),
          Map.entry("Italians", "意大利"),
          Map.entry("AI_Italians", "意大利（AI）"),
          Map.entry("Japanese", "日本"),
          Map.entry("AI_Japanese", "日本（AI）"),
          Map.entry("Americans", "美国"),
          Map.entry("AI_Americans", "美国（AI）"),
          Map.entry("British", "英国"),
          Map.entry("AI_British", "英国（AI）"),
          Map.entry("Russians", "苏联"),
          Map.entry("AI_Russians", "苏联（AI）"),
          Map.entry("French", "法国"),
          Map.entry("AI_French", "法国（AI）"),
          Map.entry("Chinese", "中国"),
          Map.entry("AI_Chinese", "中国（AI）"),
          Map.entry("Canadians", "加拿大"),
          Map.entry("AI_Canadians", "加拿大（AI）"),
          Map.entry("Australians", "澳大利亚"),
          Map.entry("AI_Australians", "澳大利亚（AI）"),
          Map.entry("Indians", "印度"),
          Map.entry("AI_Indians", "印度（AI）"),
          Map.entry("Balkans", "巴尔干"),
          Map.entry("AI_Balkans", "巴尔干（AI）"),
          Map.entry("Scandinavia", "斯堪的纳维亚"),
          Map.entry("AI_Scandinavia", "斯堪的纳维亚（AI）"),
          Map.entry("Eastern_Europe", "东欧"),
          Map.entry("AI_Eastern_Europe", "东欧（AI）"),
          Map.entry("Turkey", "土耳其"),
          Map.entry("AI_Turkey", "土耳其（AI）"),
          Map.entry("Neutral_Nations", "中立国"),
          Map.entry("Allies", "同盟国"),
          Map.entry("Axis", "轴心国"));

  private static final Map<String, String> TECH_NAMES =
      Map.ofEntries(
          Map.entry("Jet Power", "喷气动力"),
          Map.entry("Super subs", "超级潜艇"),
          Map.entry("Long Range Aircraft", "远程飞机"),
          Map.entry("Rockets Advance", "火箭技术"),
          Map.entry("destroyersCanBombard", "驱逐舰轰炸"),
          Map.entry("Heavy Bomber", "重型轰炸机"),
          Map.entry("Improved Shipyards", "改良船坞"),
          Map.entry("Industrial Technology", "工业技术"),
          Map.entry("Mechanized Infantry", "机械化步兵"),
          Map.entry("Radar", "雷达"),
          Map.entry("Guided Missiles", "制导导弹"),
          Map.entry("Combined Arms", "合成部队"));

  private static final Map<String, String> STEP_NAMES =
      Map.ofEntries(
          Map.entry("Combat Move", "战斗移动"),
          Map.entry("Non-Combat Move", "非战斗移动"),
          Map.entry("Move", "移动"),
          Map.entry("Purchase", "购买"),
          Map.entry("Place", "部署"),
          Map.entry("Tech", "科技"),
          Map.entry("Battle", "战斗"),
          Map.entry("Bid", "出价"),
          Map.entry("End Turn", "结束回合"),
          Map.entry("Give Technologies", "授予科技"),
          Map.entry("Technology", "科技"));

  private static String lookup(final Map<String, String> map, final String name) {
    return name == null ? null : map.getOrDefault(name, name);
  }

  public static String playerName(final String name) {
    return lookup(PLAYER_NAMES, name);
  }

  public static String techName(final String name) {
    return lookup(TECH_NAMES, name);
  }

  public static String stepName(final String name) {
    return lookup(STEP_NAMES, name);
  }

  /** Localizes generic words inside a territory name while keeping map-specific parts intact. */
  public static String territoryName(final String name) {
    if (name == null) {
      return null;
    }
    return name.replace("Sea Zone", "海域");
  }
}
