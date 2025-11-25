package pl.fulllegitcode.util;

import android.app.Activity;

import java.util.concurrent.ExecutorService;

import pl.fulllegitcode.flcexoplayer.Player;

public class Exo {
  public interface Callback {
    void onError(int error);

    void onPlaybackState(int state);
  }

  private static int _nextId = 1;

  private final int _id = _nextId++;
  private final Player player;
  private boolean disposed = false;

  public int id() {
    return _id;
  }

  public Exo(Activity activity, ExecutorService threadPool, String uri, Callback callback) {
    player = new Player(activity);
    player.prepare(uri, new String[]{}, false);
    threadPool.execute(() -> {
      int playbackStateOld = 0;
      while (!disposed) {
        try {
          Thread.sleep(100);
          int playbackStateNew = player.getPlaybackState();
          if (playbackStateNew != playbackStateOld) {
            playbackStateOld = playbackStateNew;
            callback.onPlaybackState(playbackStateNew);
          }
        } catch (InterruptedException ignored) {
        }
      }
    });
  }

  public void dispose() {
    disposed = true;
    player.dispose();
  }
}
