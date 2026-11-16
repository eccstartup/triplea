package tools.map.making.ui;

import games.strategy.triplea.EngineImageLoader;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import lombok.experimental.UtilityClass;
import org.triplea.swing.JButtonBuilder;
import org.triplea.swing.JFrameBuilder;
import org.triplea.swing.ScreenShareSize;
import org.triplea.swing.SwingAction;
import org.triplea.swing.SwingComponents;
import tools.map.making.ui.runnable.AutoPlacementFinderTask;
import tools.map.making.ui.runnable.CenterPickerTask;
import tools.map.making.ui.runnable.DecorationPlacerTask;
import tools.map.making.ui.runnable.ImageShrinkerTask;
import tools.map.making.ui.runnable.MapPropertiesMakerTask;
import tools.map.making.ui.runnable.PlacementPickerTask;
import tools.map.making.ui.runnable.PolygonGrabberTask;
import tools.map.making.ui.runnable.TileImageBreakerTask;
import tools.map.making.ui.runnable.TileImageReconstructorTask;

/** A frame that will run the different map making utilities we have. */
@UtilityClass
public class MapCreator {

  public static void openMapCreatorWindow() {
    final JPanel mapPropertiesPanel = MapPropertiesPanel.build();

    final JPanel mainPanel = new JPanel();
    mainPanel.add(mapPropertiesPanel);

    final JPanel sidePanel = new JPanel();
    sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
    sidePanel.add(Box.createVerticalGlue());

    // set up the menu actions
    addButtonToSidePanel(sidePanel, "第 1 步：地图属性", mainPanel, mapPropertiesPanel);
    addButtonToSidePanel(sidePanel, "第 2 步：地图工具", mainPanel, getMapSkinPanel());
    addButtonToSidePanel(sidePanel, "第 3 步：游戏 XML", mainPanel, XmlUtilitiesPanel.build());
    addButtonToSidePanel(sidePanel, "第 4 步：验证地图", mainPanel, ValidateMapPanel.build());
    addButtonToSidePanel(sidePanel, "其他：可选功能", mainPanel, getOptionalUtilitiesPanel());

    final JFrame frame =
        new JFrameBuilder()
            .title("TripleA 地图制作工具")
            .iconImage(EngineImageLoader.loadFrameIcon())
            .disposeOnClose()
            .layout(new BorderLayout())
            .build();

    final Container contentPane = frame.getContentPane();
    contentPane.add(new JScrollPane(sidePanel), BorderLayout.WEST);
    contentPane.add(new JScrollPane(mainPanel), BorderLayout.CENTER);

    frame.pack();
    ScreenShareSize.apply(frame, 0.6, 0.7);
    frame.setVisible(true);
  }

  private static JPanel getOptionalUtilitiesPanel() {
    return MapMakingPanelFactory.get(
        "其他或可选工具：",
        new MapMakingPanelFactory.ButtonSpec("运行图像缩小器", ImageShrinkerTask::run),
        new MapMakingPanelFactory.ButtonSpec("运行图块图像重建器", TileImageReconstructorTask::run));
  }

  private static JPanel getMapSkinPanel() {
    return MapMakingPanelFactory.get(
        "地图皮肤工具：",
        new MapMakingPanelFactory.ButtonSpec("运行地图属性生成器", MapPropertiesMakerTask::run),
        new MapMakingPanelFactory.ButtonSpec("运行中心点选择器", CenterPickerTask::run),
        new MapMakingPanelFactory.ButtonSpec("运行多边形抓取器", PolygonGrabberTask::run),
        new MapMakingPanelFactory.ButtonSpec("运行自动部署点查找器", AutoPlacementFinderTask::run),
        new MapMakingPanelFactory.ButtonSpec("运行部署点选择器", PlacementPickerTask::run),
        new MapMakingPanelFactory.ButtonSpec("运行图块图像分割器", TileImageBreakerTask::run),
        new MapMakingPanelFactory.ButtonSpec("运行装饰物放置器", DecorationPlacerTask::run));
  }

  private static void addButtonToSidePanel(
      JPanel sidePanel, String labelText, JPanel mainPanel, JPanel panel) {
    JButton button =
        new JButtonBuilder(labelText)
            .actionListener(() -> swapContainerContents(mainPanel, panel))
            .build();
    // buttons stretch to the widest one, so every label shows in full and the column lines up
    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
    sidePanel.add(button);
    sidePanel.add(Box.createVerticalGlue());
  }

  private void swapContainerContents(final Container container, final JPanel panel) {
    container.removeAll();
    container.add(panel);
    SwingAction.invokeNowOrLater(() -> SwingComponents.redraw(container));
  }
}
