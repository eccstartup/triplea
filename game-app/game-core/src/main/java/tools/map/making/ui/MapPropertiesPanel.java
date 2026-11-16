package tools.map.making.ui;

import games.strategy.triplea.UrlConstants;
import games.strategy.triplea.image.UnitImageFactory;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import lombok.experimental.UtilityClass;
import org.triplea.awt.OpenFileUtility;
import org.triplea.swing.JButtonBuilder;
import org.triplea.swing.JTextAreaBuilder;
import org.triplea.swing.SwingAction;
import org.triplea.swing.jpanel.JPanelBuilder;
import tools.util.FileSave;
import tools.util.ToolArguments;

@UtilityClass
public class MapPropertiesPanel {

  private static final int PANEL_LINE_SPACING = 23;

  public JPanel build() {
    final JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    addBlockToPanel(panel);
    addBlockToPanel(
        panel,
        new JScrollPane(
            new JTextAreaBuilder()
                .rows(15)
                .columns(45)
                .text(
                    """
                        欢迎使用 Veqryn 的 TripleA 地图制作程序。\


                        开始之前，请先在您的目录中创建一个文件夹：\
                        Users\\您的用户名\\triplea\\maps\

                        请用地图的简短名称命名该文件夹，名称中不要使用任何特殊字符。\

                        接下来，在地图文件夹内创建 5 个子文件夹，名称分别为：\
                        flags、units、baseTiles、reliefTiles、games\

                        然后创建一个文本文件并将其重命名为 "map.properties"，或使用本工具生成的文件。\


                        要开始使用地图工具，请准备一张地图的 png 图像，其中只包含 \
                        领地边界，不含其他内容。边界必须为黑色（十六进制：000000），且线条或边缘 \
                        不得有任何抗锯齿（平滑）处理或外突。\

                        创建一张地图的小图（约 250 像素宽），命名为 \
                        "smallMap.jpeg"。\

                        将这些文件放入地图的根文件夹。现在您可以在下方填写详细信息来启动地图制作工具，\
                        然后再进入“第 2 步”并运行地图工具。""")
                .build()));
    addBlockToPanel(
        panel,
        new JButtonBuilder()
            .title("开始教程  /  显示帮助文档")
            .toolTip("打开关于如何制作地图的说明文件")
            .actionListener(
                () ->
                    OpenFileUtility.openUrl(
                        JOptionPane.getFrameForComponent(panel), UrlConstants.MAP_MAKER_HELP))
            .build());
    addBlockToPanel(
        panel,
        new JButtonBuilder()
            .title("选择地图文件夹")
            .actionListener(
                () ->
                    SwingAction.of(
                        "选择地图文件夹",
                        e -> {
                          final Path mapFolder =
                              new FileSave(
                                      "你的地图文件夹在哪里？",
                                      null,
                                      ToolArguments.getPropertyMapFolderPath().orElse(null))
                                  .getFile();
                          if (mapFolder != null && Files.exists(mapFolder)) {
                            ToolArguments.MAP_FOLDER.setSystemProperty(mapFolder.toString());
                          }
                        }))
            .build());
    addInputFieldToPanel(
        panel,
        "单位缩放： ",
        "为单位图像选择缩放系数。\n示例：1.25、1、0.875、0.8333、0.75、0.6666、0.5625、0.5",
        "0.75",
        ToolArguments.UNIT_ZOOM);
    addInputFieldToPanel(
        panel,
        "单位宽度： ",
        "设置单位图像的宽度",
        Integer.toString(UnitImageFactory.DEFAULT_UNIT_ICON_SIZE),
        ToolArguments.UNIT_WIDTH);
    addInputFieldToPanel(
        panel,
        "单位高度： ",
        "设置单位图像的高度",
        Integer.toString(UnitImageFactory.DEFAULT_UNIT_ICON_SIZE),
        ToolArguments.UNIT_HEIGHT);
    return panel;
  }

  private static void addInputFieldToPanel(
      JPanel panel, String label, String tooltip, String text, ToolArguments toolsArgument) {
    final JTextField toolsArgumentText = new JTextField(text);
    Dimension fieldDimension = new Dimension(45, 20);
    toolsArgumentText.setPreferredSize(fieldDimension);
    toolsArgumentText.setMinimumSize(fieldDimension);
    toolsArgumentText.setMaximumSize(fieldDimension);
    toolsArgumentText.addFocusListener(
        new FocusListener() {
          @Override
          public void focusGained(final FocusEvent e) {
            /* interface method, but nothing to be done when gaining focus */
          }

          @Override
          public void focusLost(final FocusEvent e) {
            toolsArgument.setSystemProperty(toolsArgumentText.getText());
            toolsArgumentText.setText(toolsArgument.getSystemPropertyValuePlain());
          }
        });
    toolsArgumentText.setToolTipText(tooltip);
    JLabel textFieldLabel = new JLabel(label);
    Dimension labelDimension = new Dimension(80, 20);
    textFieldLabel.setPreferredSize(labelDimension);
    textFieldLabel.setMaximumSize(labelDimension);
    textFieldLabel.setMinimumSize(labelDimension);
    addBlockToPanel(
        panel,
        new JPanelBuilder()
            .boxLayoutHorizontal()
            .add(textFieldLabel)
            .add(toolsArgumentText)
            .build());
  }

  private static void addBlockToPanel(JPanel panel, JComponent... components) {
    for (JComponent component : components) {
      component.setAlignmentX(Component.LEFT_ALIGNMENT);
      panel.add(component);
    }
    panel.add(Box.createVerticalStrut(PANEL_LINE_SPACING));
  }
}
