package tools.map.making.ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import lombok.experimental.UtilityClass;
import org.triplea.swing.JButtonBuilder;
import tools.map.making.ui.runnable.ConnectionFinderTask;

@UtilityClass
public class XmlUtilitiesPanel {

  private static final int SPACING_HEIGHT = 30;

  public JPanel build() {
    final JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    panel.add(Box.createVerticalStrut(SPACING_HEIGHT));
    panel.add(new JLabel("游戏 XML 工具："));
    panel.add(Box.createVerticalStrut(SPACING_HEIGHT));
    panel.add(new JLabel("抱歉，目前唯一的 XML 生成器是 Wisconsin 地图制作工具中的“第二部分”。"));
    panel.add(Box.createVerticalStrut(SPACING_HEIGHT));
    panel.add(new JButtonBuilder("运行连接查找器").actionListener(ConnectionFinderTask::run).build());
    panel.add(Box.createVerticalStrut(SPACING_HEIGHT));
    return panel;
  }
}
