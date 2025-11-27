package pl.fulllegitcode.util;

import android.app.Activity;

import java.util.concurrent.ExecutorService;

import pl.fulllegitcode.flcexoplayer.Player;

public class Exo {
  public interface Callback {
    void onDispose();
    void onError(int error);
    void onImageAvailable(int width, int height);
    void onPlaybackState(int state);
  }

  private static int _nextId = 1;

  private final int _id = _nextId++;
  private final Callback callback;
  private final Player player;
  int error = -1;
  int playbackState = 0;
  private boolean disposed = false;
  private boolean imageAvailable = false;

  public int id() {
    return _id;
  }

  public Exo(Activity activity, ExecutorService threadPool, String uri, Callback callback) {
    this.callback = callback;
    player = new Player(activity);
    player.prepare(uri, new String[]{}, false);
    threadPool.execute(() -> {
      while (!disposed) {
        try {
          activity.runOnUiThread(() -> {
            if (disposed)
              return;
            int errorNew = player.getPlaybackErrorType();
            if (errorNew != error) {
              error = errorNew;
              callback.onError(errorNew);
            }
            int playbackStateNew = player.getPlaybackState();
            if (playbackStateNew != playbackState) {
              playbackState = playbackStateNew;
              callback.onPlaybackState(playbackStateNew);
            }
            if (!imageAvailable && player.isImageAvailable()) {
              imageAvailable = true;
              callback.onImageAvailable(player.getImageWidth(), player.getImageHeight());
            }
          });
          Thread.sleep(250);
        } catch (InterruptedException ignored) {
        }
      }
    });
  }

  public byte[] getFrame() {
    return player.getImageBytes();
  }

  public void setPlaying(boolean playing) {
    player.setPlayWhenReady(playing);
  }

  public void dispose() {
    disposed = true;
    player.dispose();
    callback.onDispose();
  }
}
