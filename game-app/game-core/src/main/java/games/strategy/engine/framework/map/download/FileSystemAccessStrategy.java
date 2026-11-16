package games.strategy.engine.framework.map.download;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.triplea.swing.SwingComponents;

@UtilityClass
class FileSystemAccessStrategy {

  void remove(
      final Component parentFrame,
      final Function<ManagedMap, Boolean> mapDeleteAction,
      final List<ManagedMap> toRemove,
      final Consumer<List<ManagedMap>> removeAction) {
    SwingComponents.promptUser(
        "移除地图？",
        "<html>即将移除 "
            + toRemove.size()
            + " 个地图，确定吗？<br/>"
            + formatMapList(toRemove, ManagedMap::getMapName)
            + "</html>",
        newRemoveMapAction(parentFrame, mapDeleteAction, toRemove, removeAction));
  }

  private static Runnable newRemoveMapAction(
      final Component parentFrame,
      final Function<ManagedMap, Boolean> mapDeleteAction,
      final List<ManagedMap> maps,
      final Consumer<List<ManagedMap>> removeActionListeners) {
    return () -> {
      final List<ManagedMap> deletes = new ArrayList<>();

      // delete the map files
      for (final ManagedMap map : maps) {
        if (mapDeleteAction.apply(map)) {
          deletes.add(map);
        }
      }

      if (!deletes.isEmpty()) {
        removeActionListeners.accept(deletes);
        final String message = newDialogMessage("移除成功。", deletes);
        showDialog(parentFrame, message, deletes, ManagedMap::getMapName);
      }
    };
  }

  private static void showDialog(
      final Component parentFrame,
      final String message,
      final List<ManagedMap> mapList,
      final Function<ManagedMap, String> outputFunction) {

    SwingComponents.newMessageDialog(
        parentFrame,
        "<html>" + message + "<br /> " + formatMapList(mapList, outputFunction) + "</html>");
  }

  private static String newDialogMessage(final String message, final List<ManagedMap> mapList) {
    final String plural = "";
    return message + " " + mapList.size() + " 张地图" + plural;
  }

  private static String formatMapList(
      final List<ManagedMap> mapList, final Function<ManagedMap, String> outputFunction) {
    final int maxMapsToList = 6;
    final StringBuilder sb = new StringBuilder("<ul>");
    for (int i = 0; i < mapList.size(); i++) {
      if (i > maxMapsToList) {
        sb.append("<li>...</li>");
        break;
      }
      sb.append("<li>").append(outputFunction.apply(mapList.get(i))).append("</li>");
    }
    return sb.toString();
  }
}
