package tools.map.making.ui;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.gameparser.GameParser;
import java.awt.Component;
import java.awt.Dimension;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Optional;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import lombok.experimental.UtilityClass;
import org.triplea.swing.JButtonBuilder;
import org.triplea.swing.JTextAreaBuilder;
import tools.util.FileOpen;
import tools.util.ToolArguments;

@UtilityClass
public class ValidateMapPanel {

  Path gameXmlPath = null;

  public JPanel build() {
    final JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(Box.createVerticalStrut(30));
    JLabel mainLabel = new JLabel("地图验证工具：");
    //    mainLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, mainLabel.getPreferredSize().height));

    panel.add(mainLabel);
    //    panel.add(Box.createHorizontalGlue()); // push label left
    panel.add(Box.createVerticalStrut(30));
    final JLabel gameXmlLabel = new JLabel("");
    final JTextArea parseResultText =
        new JTextAreaBuilder().rows(15).columns(45).text("未选择要验证的游戏 XML。").build();
    parseResultText.setAlignmentX(Component.LEFT_ALIGNMENT);
    parseResultText.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, parseResultText.getPreferredSize().height));
    panel.add(
        new JButtonBuilder("验证游戏 XML 文件")
            .toolTip("开始尝试解析游戏 XML 文件并给出结果")
            .actionListener(
                e -> {
                  gameXmlPath =
                      new FileOpen(
                              "选择游戏 XML 文件",
                              (gameXmlPath != null
                                  ? gameXmlPath.getParent()
                                  : ToolArguments.getPropertyMapFolderPath().orElse(null)),
                              ".xml")
                          .getFile();
                  if (gameXmlPath != null) {
                    gameXmlLabel.setText(gameXmlPath.toString());
                    try {
                      Optional<GameData> optionalGameData = GameParser.parse(gameXmlPath, false);
                      if (optionalGameData.isPresent()) {
                        parseResultText.setText("✅ 地图有效");
                      } else {
                        parseResultText.setText("❌ 验证失败");
                      }
                    } catch (InvalidPathException ex) {
                      final String message =
                          "错误： "
                              + ex.getClass().getSimpleName()
                              + " — "
                              + (ex.getMessage() != null ? ex.getMessage() : "无详细信息");
                      parseResultText.setText("❌ 验证失败：%s".formatted(message));
                    }
                  } else {
                    parseResultText.setText("未选择要验证的游戏 XML。");
                  }
                  SwingUtilities.invokeLater(panel::repaint);
                })
            .build());
    //    panel.add(Box.createHorizontalGlue()); // push label left
    panel.add(Box.createVerticalStrut(30));
    panel.add(gameXmlLabel);
    panel.add(Box.createVerticalStrut(30));
    panel.add(parseResultText);
    panel.add(Box.createVerticalStrut(30));
    return panel;
  }
}
