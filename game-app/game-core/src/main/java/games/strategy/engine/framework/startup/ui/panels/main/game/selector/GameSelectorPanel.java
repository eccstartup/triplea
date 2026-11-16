package games.strategy.engine.framework.startup.ui.panels.main.game.selector;

import static org.triplea.swing.SwingComponents.DialogWithLinksParams;
import static org.triplea.swing.SwingComponents.DialogWithLinksTypes;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.properties.IEditableProperty;
import games.strategy.engine.data.properties.PropertiesUi;
import games.strategy.engine.framework.map.download.DownloadMapsWindow;
import games.strategy.engine.framework.map.file.system.loader.InstalledMapsListing;
import games.strategy.engine.framework.startup.mc.ClientModel;
import games.strategy.engine.framework.startup.ui.FileBackedGamePropertiesCache;
import games.strategy.engine.framework.startup.ui.IGamePropertiesCache;
import games.strategy.engine.framework.system.SystemProperties;
import games.strategy.engine.framework.ui.GameChooser;
import games.strategy.engine.framework.ui.background.BackgroundTaskRunner;
import games.strategy.engine.framework.ui.background.TaskRunner;
import games.strategy.triplea.EngineImageLoader;
import games.strategy.triplea.UrlConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.triplea.config.product.ProductVersionReader;
import org.triplea.swing.DialogBuilder;
import org.triplea.swing.JButtonBuilder;
import org.triplea.swing.SwingAction;
import org.triplea.swing.SwingComponents;
import org.triplea.swing.jpanel.GridBagConstraintsBuilder;

/**
 * Left hand side panel of the launcher screen that has various info, like selected game and engine
 * version.
 */
public final class GameSelectorPanel extends JPanel implements Observer {
  private static final long serialVersionUID = -4598107601238030020L;

  private final GameSelectorModel model;
  private final IGamePropertiesCache gamePropertiesCache = new FileBackedGamePropertiesCache();
  private final Map<String, Object> originalPropertiesMap = new HashMap<>();
  private final JLabel nameText = new JLabel();
  private final JLabel saveGameText = new JLabel();
  private final JLabel roundText = new JLabel();

  private final JButton loadNewGame =
      new JButtonBuilder().title("选择游戏").toolTip("开始新游戏（机器人服务器上的自动存档会显示在此处）").build();

  private final JButton loadSavedGame =
      new JButtonBuilder().title("已保存的游戏").toolTip("打开电脑上已保存的游戏。").build();

  private final JButton mapOptions =
      new JButtonBuilder()
          .title("游戏选项")
          .toolTip("<html>设置当前所选游戏的选项，<br>例如开启/关闭 " + "低运气或科技</html>")
          .build();

  public GameSelectorPanel(final GameSelectorModel model) {
    this.model = model;
    final GameData data = model.getGameData();
    if (data != null) {
      setOriginalPropertiesMap(data);
      gamePropertiesCache.loadCachedGamePropertiesInto(data);
    }

    setLayout(new GridBagLayout());

    final JLabel logoLabel =
        new JLabel(
            new ImageIcon(EngineImageLoader.loadImage("launch_screens", "triplea-logo.png")));

    int row = 0;
    add(
        logoLabel,
        new GridBagConstraintsBuilder(0, row)
            .gridWidth(2)
            .insets(new Insets(10, 10, 3, 5))
            .build());
    row++;

    add(new JLabel("Java 版本："), buildGridCell(0, row, new Insets(10, 10, 3, 5)));
    add(
        new JLabel(SystemProperties.getJavaVersion()),
        buildGridCell(1, row, new Insets(10, 0, 3, 0)));
    row++;

    add(new JLabel("引擎版本："), buildGridCell(0, row, new Insets(0, 10, 3, 5)));
    add(
        new JLabel(ProductVersionReader.getCurrentVersion().toString()),
        buildGridCell(1, row, new Insets(0, 0, 3, 0)));
    row++;

    add(new JLabel("游戏名称："), buildGridCell(0, row, new Insets(0, 10, 3, 5)));
    add(nameText, buildGridCell(1, row, new Insets(0, 0, 3, 0)));
    row++;

    add(new JLabel("游戏回合："), buildGridCell(0, row, new Insets(0, 10, 3, 5)));
    add(roundText, buildGridCell(1, row, new Insets(0, 0, 3, 0)));
    row++;

    add(new JLabel("已加载的存档："), buildGridCell(0, row, new Insets(20, 10, 3, 5)));
    row++;

    add(saveGameText, buildGridRow(0, row, new Insets(0, 10, 3, 5)));
    row++;

    add(loadNewGame, buildGridRow(0, row, new Insets(25, 10, 10, 10)));
    row++;

    add(loadSavedGame, buildGridRow(0, row, new Insets(0, 10, 10, 10)));
    row++;

    final JButton downloadMapButton =
        new JButtonBuilder()
            .title("下载地图")
            .toolTip("点击此按钮安装更多地图")
            .actionListener(DownloadMapsWindow::showDownloadMapsWindow)
            .build();
    add(downloadMapButton, buildGridRow(0, row, new Insets(0, 10, 10, 10)));
    row++;

    add(mapOptions, buildGridRow(0, row, new Insets(25, 10, 10, 10)));
    row++;

    // spacer
    add(
        new JPanel(),
        new GridBagConstraints(
            0,
            row,
            2,
            1,
            1,
            1,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0),
            0,
            0));

    loadNewGame.addActionListener(
        e -> {
          if (canSelectLocalGameData()) {
            selectGameFile();
          } else if (canChangeHostBotGameData()) {
            final ClientModel clientModelForHostBots = model.getClientModelForHostBots();
            if (clientModelForHostBots != null) {
              clientModelForHostBots.setMap(this);
            }
          }
        });

    loadSavedGame.addActionListener(
        e -> {
          if (canSelectLocalGameData()) {
            selectSavedGameFile();
          } else if (canChangeHostBotGameData()) {
            final ClientModel clientModelForHostBots = model.getClientModelForHostBots();
            if (clientModelForHostBots != null) {
              clientModelForHostBots.executeChangeGameToSaveGameClientAction(
                  JOptionPane.getFrameForComponent(this));
            }
          }
        });

    mapOptions.addActionListener(
        e -> {
          if (canSelectLocalGameData()) {
            selectGameOptions();
          } else if (canChangeHostBotGameData()) {
            final ClientModel clientModelForHostBots = model.getClientModelForHostBots();
            if (clientModelForHostBots != null) {
              clientModelForHostBots.changeGameOptions(this);
            }
          }
        });

    updateGameData();
  }

  private static GridBagConstraints buildGridCell(final int x, final int y, final Insets insets) {
    return buildGrid(x, y, insets, 1);
  }

  private static GridBagConstraints buildGridRow(final int x, final int y, final Insets insets) {
    return buildGrid(x, y, insets, 2);
  }

  private static GridBagConstraints buildGrid(
      final int x, final int y, final Insets insets, final int width) {
    final int gridHeight = 1;
    final double weightX = 0;
    final double weightY = 0;
    final int anchor = GridBagConstraints.WEST;
    final int fill = GridBagConstraints.NONE;
    final int ipadx = 0;
    final int ipady = 0;

    return new GridBagConstraints(
        x, y, width, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady);
  }

  private void setOriginalPropertiesMap(final GameData data) {
    originalPropertiesMap.clear();
    if (data != null) {
      for (final IEditableProperty<?> property : data.getProperties().getEditableProperties()) {
        originalPropertiesMap.put(property.getName(), property.getValue());
      }
    }
  }

  private void selectGameOptions() {
    // backup current game properties before showing dialog
    final Map<String, Object> currentPropertiesMap = new HashMap<>();
    for (final IEditableProperty<?> property :
        model.getGameData().getProperties().getEditableProperties()) {
      currentPropertiesMap.put(property.getName(), property.getValue());
    }
    final PropertiesUi panel = new PropertiesUi(model.getGameData().getProperties(), true);
    final JScrollPane scroll = new JScrollPane(panel);
    scroll.setBorder(null);
    scroll.getViewport().setBorder(null);
    final JOptionPane pane = new JOptionPane(scroll, JOptionPane.PLAIN_MESSAGE);
    final String ok = "OK";
    final String cancel = "Cancel";
    final String makeDefault = "Make Default";
    final String reset = "Reset";
    pane.setOptions(new Object[] {ok, makeDefault, reset, cancel});
    final JDialog window = pane.createDialog(JOptionPane.getFrameForComponent(this), "游戏选项");
    window.setVisible(true);
    final Object buttonPressed = pane.getValue();
    if (buttonPressed == null || buttonPressed.equals(cancel)) {
      // restore properties, if cancel was pressed, or window was closed
      for (final IEditableProperty<?> property :
          model.getGameData().getProperties().getEditableProperties()) {
        property.validateAndSet(currentPropertiesMap.get(property.getName()));
      }
    } else if (buttonPressed.equals(reset)) {
      if (!originalPropertiesMap.isEmpty()) {
        // restore properties, if cancel was pressed, or window was closed
        for (final IEditableProperty<?> property :
            model.getGameData().getProperties().getEditableProperties()) {
          property.validateAndSet(originalPropertiesMap.get(property.getName()));
        }
        selectGameOptions();
      }
    } else if (buttonPressed.equals(makeDefault)) {
      gamePropertiesCache.cacheGameProperties(model.getGameData());
    }
  }

  private void updateGameData() {
    SwingAction.invokeNowOrLater(
        () -> {
          nameText.setText(model.getGameName());
          roundText.setText(model.getGameRound());
          saveGameText.setText(model.getFileName());

          final boolean canSelectGameData = canSelectLocalGameData();
          final boolean canChangeHostBotGameData = canChangeHostBotGameData();
          loadSavedGame.setEnabled(canSelectGameData || canChangeHostBotGameData);
          loadNewGame.setEnabled(canSelectGameData || canChangeHostBotGameData);
          // Disable game options if there are none.
          mapOptions.setEnabled(
              canChangeHostBotGameData
                  || (canSelectGameData
                      && model.getGameData() != null
                      && !model.getGameData().getProperties().getEditableProperties().isEmpty()));
        });
  }

  private boolean canSelectLocalGameData() {
    return model != null && model.isCanSelect();
  }

  private boolean canChangeHostBotGameData() {
    return model != null && model.isHostIsHeadlessBot();
  }

  @Override
  public void update(final Observable o, final Object arg) {
    updateGameData();
  }

  private void selectSavedGameFile() {
    GameFileSelector.builder()
        .fileDoesNotExistAction(
            file ->
                DialogBuilder.builder()
                    .parent(this)
                    .title("未找到游戏存档文件")
                    .errorMessage("文件不存在：" + file.toAbsolutePath())
                    .showDialog())
        .build()
        .selectGameFile(JOptionPane.getFrameForComponent(this))
        .ifPresent(this::loadSaveFile);
  }

  public void loadSaveFile(final Path file) {
    TaskRunner.builder()
        .waitDialogTitle("正在加载存档")
        .exceptionHandler(
            e ->
                SwingComponents.showDialogWithLinks(
                    DialogWithLinksParams.builder()
                        .title("加载存档失败")
                        .dialogType(DialogWithLinksTypes.ERROR)
                        .dialogText(
                            String.format(
                                "<html>Error: %s<br/><br/>"
                                    + "如果这不是预期结果，请 "
                                    + "提交一份 <a href=%s>错误报告</a><br/>"
                                    + "并附上上述错误消息以及你正在尝试加载的 "
                                    + "游戏存档。",
                                e.getMessage(), UrlConstants.GITHUB_ISSUES))
                        .build()))
        .build()
        .run(
            () -> {
              if (model.loadSave(file)) {
                setOriginalPropertiesMap(model.getGameData());
              }
            });
  }

  private void selectGameFile() {
    try {
      final InstalledMapsListing installedMapsListing =
          BackgroundTaskRunner.runInBackgroundAndReturn(
              "正在加载所有可用游戏……", InstalledMapsListing::parseMapFiles);

      GameChooser.chooseGame(
          JOptionPane.getFrameForComponent(this),
          installedMapsListing,
          model.getGameName(),
          this::gameSelected);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void gameSelected(final Path gameFile) {
    BackgroundTaskRunner.runInBackground(
        "正在加载地图……",
        () -> {
          if (model.loadMap(gameFile)) {
            setOriginalPropertiesMap(model.getGameData());
            // only for new games, not saved games, we set the default options, and set them only
            // once (the first time it is loaded)
            gamePropertiesCache.loadCachedGamePropertiesInto(model.getGameData());
          }
        });
  }
}
