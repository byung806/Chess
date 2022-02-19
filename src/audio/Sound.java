package audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {
    public final Clip PLAYER_MOVE;
    public final Clip OPPONENT_MOVE;
    public final Clip CASTLE_MOVE;
    public final Clip CAPTURE_MOVE;
    public final Clip CHECK_MOVE;

    public Sound() throws Exception {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(new File("assets/sounds/move.wav")));
        PLAYER_MOVE = clip;
        clip.open(AudioSystem.getAudioInputStream(new File("assets/sounds/move_opponent.wav")));
        OPPONENT_MOVE = clip;
        clip.open(AudioSystem.getAudioInputStream(new File("assets/sounds/capture.wav")));
        CAPTURE_MOVE = clip;
        clip.open(AudioSystem.getAudioInputStream(new File("assets/sounds/castle.wav")));
        CASTLE_MOVE = clip;
        clip.open(AudioSystem.getAudioInputStream(new File("assets/sounds/check.wav")));
        CHECK_MOVE = clip;
        clip.close();
    }

    public void play(Clip clip) {
        clip.start();
    }
}
