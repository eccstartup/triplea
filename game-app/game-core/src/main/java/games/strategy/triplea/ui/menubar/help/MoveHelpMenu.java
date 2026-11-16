package games.strategy.triplea.ui.menubar.help;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import lombok.experimental.UtilityClass;
import org.triplea.swing.SwingAction;

@UtilityClass
class MoveHelpMenu {

  Action buildMenu() {
    return SwingAction.of(
        "移动/选择",
        e -> {
          // html formatted string
          final JEditorPane editorPane = new JEditorPane();
          editorPane.setEditable(false);
          editorPane.setContentType("text/html");
          // Render CSS lengths per W3C spec instead of Swing's legacy ~1.33x inflation. Issue
          // #6157.
          editorPane.putClientProperty(JEditorPane.W3C_LENGTH_UNITS, Boolean.TRUE);
          final String hints =
              "<font size='+1'>选择单位</font><br>"
                  + "点击单位堆会将其中 <u>1 个单位</u> 加入选择。<br>"
                  + "<b>CTRL + 点击单位堆</b> 会选中 <u>该堆中的所有单位</u>。<br>"
                  + "<b>Shift + 点击领地/海域</b> 会选中 "
                  + "<u>该区域中的所有单位</u>。<br>"
                  + "<b>点击某个区域</b>（不是单位）可 <u>从中选择单位</u>。<br>"
                  + "<b>Alt + 点击单位堆</b> 会选中堆中的 <u>10 个单位</u>。<br>"
                  + "<br><font size='+1'> 取消选择单位</font><br>"
                  + "<b>Escape</b> 取消选择 <u>所有已选单位</u>。<br>"
                  + "<b>右键点击</b> 单位堆会取消选择堆中的 <u>1 个单位</u>。<br>"
                  + "<b>CTRL + 右键点击</b> 单位堆会取消选择堆中的 <u>所有单位</u>。<br>"
                  + "<b>右键点击</b>（不在单位上）会取消选择 "
                  + "<u>最后选中的单位</u>。<br>"
                  + "<b>Alt + 右键点击</b> 单位堆会取消选择堆中的 <u>10 个单位</u>。<br>"
                  + "<br><font size='+1'> 移动单位</font><br>"
                  + "选中单位后，直接点击要移动到的区域即可。<br>"
                  + "<b>CTRL + 点击</b> 某区域可将其选为 <u>途经点</u>：<br>"
                  + "单位将经由此区域以最短路径移动到 "
                  + "目的地。<br>"
                  + "<br><font size='+1'> 在屏幕上移动地图</font><br>"
                  + "可在菜单 游戏><b>引擎设置：</b> "
                  + "地图滚动 中调整参数。<br>"
                  + "<b>滚动鼠标滚轮</b> 可垂直移动地图，<br>"
                  + "<b>按住 Shift + 滚动滚轮</b> 可水平移动地图。<br>"
                  + "<b>按住右键</b> 并移动鼠标可 <u>拖动地图</u>。<br>"
                  + "点击地图（任意位置），<b>使用方向键</b> "
                  + "即可移动地图，<br>"
                  + "<b>CTRL + 方向键</b> 可更快移动地图。<br>"
                  + "<b>点击屏幕右上角的小地图</b>，"
                  + "再拖动鼠标（也可不拖）。<br>"
                  + "除非已禁用，否则 <b>将鼠标移到边缘附近</b>"
                  + " 会向该方向滚动地图。<br>"
                  + "<br>"
                  + "<font size='+1'> 更改地图缩放</font><br>"
                  + "<b>菜单 视图>缩放</b> 选择所需缩放级别（最大 100%）。<br>"
                  + "<b>按住 CTRL</b> 配合 -/+ 或滚动鼠标滚轮。<br>"
                  + "<br><font size='+1'> 其他提示</font><br>"
                  + "按 <b>F</b> 高亮 <u>所有还有移动力的单位</u> "
                  + "（仅限移动阶段）。<br>"
                  + "鼠标悬停在单位上时按 <b>U</b> 可 "
                  + "<u>撤销该单位的所有移动</u>。<br>";
          editorPane.setText(hints);
          final JScrollPane scroll = new JScrollPane(editorPane);
          JOptionPane.showMessageDialog(null, scroll, "移动/选择帮助（键盘与鼠标）", JOptionPane.PLAIN_MESSAGE);
        });
  }
}
