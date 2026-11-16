package games.strategy.engine.framework;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.jetbrains.annotations.NonNls;

/**
 * This is the main i18n (internationalization) class to retrieve language dependent outputs. The
 * subclasses of this class should implement a singleton pattern and define the path to the resource
 * property file(s).
 */
public abstract class I18nResourceBundle {
  @NonNls private static final String LANGUAGE_PROPERTY = "triplea.language";
  private static final Map<Locale, Double> mapSupportedLocales = getNewMapSupportedLocales();

  protected I18nResourceBundle() {}

  /**
   * Add new supported locales here!
   *
   * @return new map supported Locale->weight Double
   */
  private static Map<Locale, Double> getNewMapSupportedLocales() {
    final Map<Locale, Double> newMapSupportedLocales = new HashMap<>();
    newMapSupportedLocales.put(Locale.US, 1.0);
    newMapSupportedLocales.put(Locale.SIMPLIFIED_CHINESE, 0.8);
    newMapSupportedLocales.put(Locale.GERMANY, 0.5);
    return newMapSupportedLocales;
  }

  public static Set<Locale> getMapSupportedLocales() {
    return mapSupportedLocales.keySet();
  }

  public static List<Locale.LanguageRange> getSupportedLanguageRange() {
    @NonNls final StringBuilder sb = new StringBuilder();
    for (final var entry : mapSupportedLocales.entrySet()) {
      sb.append(",").append(entry.getKey().toLanguageTag()).append(";q=").append(entry.getValue());
    }
    return Locale.LanguageRange.parse(sb.substring(1));
  }

  /**
   * Returns the locale used to load translations. Defaults to Simplified Chinese and can be
   * overridden with the {@code triplea.language} system property (e.g. {@code
   * -Dtriplea.language=en_US}).
   */
  public static Locale getActiveLocale() {
    @NonNls final String language = System.getProperty(LANGUAGE_PROPERTY, "zh_CN");
    return mapSupportedLocales.keySet().stream()
        .filter(
            locale -> locale.toString().equals(language) || locale.toLanguageTag().equals(language))
        .findFirst()
        .orElse(Locale.SIMPLIFIED_CHINESE);
  }

  /**
   * Switches the active language for subsequently created UI text. Components already on screen
   * keep their previous text until the UI is rebuilt or the application is restarted.
   */
  public static void setActiveLocale(final Locale locale) {
    if (!mapSupportedLocales.containsKey(locale)) {
      throw new IllegalArgumentException("Unsupported locale: " + locale);
    }
    System.setProperty(LANGUAGE_PROPERTY, locale.toString());
  }

  /**
   * @return List of supported languages
   */
  public static List<Locale> getSupportedLanguages() {
    return List.of(Locale.US, Locale.SIMPLIFIED_CHINESE, Locale.GERMANY);
  }

  public String getBaseBundleName() {
    final ResourceBundle bundle = ResourceBundle.getBundle(getResourcePath(), getActiveLocale());
    return bundle.getBaseBundleName();
  }

  /**
   * @param locale which should be checked whether it is supported
   * @return True value, if it is supported, false value otherwise
   */
  public static boolean isSupported(final Locale locale) {
    final Locale[] availableLocales = Locale.getAvailableLocales();
    return Arrays.asList(availableLocales).contains(locale);
  }

  /**
   * @return Path to resource bundle property file
   */
  protected abstract String getResourcePath();

  /**
   * @param key Bundle property key
   * @param arguments formatting arguments
   * @return Language dependent text
   */
  public String getText(final String key, final Object... arguments) {
    return String.format(getText(key), arguments);
  }

  /**
   * @param key Bundle property key
   * @return Language dependent text
   */
  public String getText(final String key) {
    final ResourceBundle bundle = ResourceBundle.getBundle(getResourcePath(), getActiveLocale());
    return bundle.getString(key);
  }
}
