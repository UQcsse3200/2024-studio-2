package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.services.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class AnimationRenderWithAudioComponent extends AnimationRenderComponent {
  Map<String, Map<Integer, String>> soundAtFrames;
  private int lastIndex = -1;


  /**
   * Create the component for a given texture atlas.
   * @param atlas libGDX-supported texture atlas containing desired animations
   */
  public AnimationRenderWithAudioComponent(TextureAtlas atlas) {
    super(atlas);
    soundAtFrames = new HashMap<>();
  }

  public void addSound(String sound, String animationName, int index) {
    if (!soundAtFrames.containsKey(animationName)) {
      soundAtFrames.put(animationName, new HashMap<>());
    }
    soundAtFrames.get(animationName).put(index, sound);
  }

  /**
   * Start playback of an animation. The animation must have been added using addAnimation().
   * @param name Name of the animation to play.
   */
  @Override
  public void startAnimation(String name) {
    lastIndex=-1;
    super.startAnimation(name);
  }

  @Override
  protected void draw(SpriteBatch batch) {
    super.draw(batch);
    if (currentAnimation == null)
    {
      return;
    }
    int index = currentAnimation.getKeyFrameIndex(animationPlayTime);
    if (index == lastIndex) {
      return;
    }
    if (soundAtFrames.containsKey(currentAnimationName)
            && soundAtFrames.get(currentAnimationName).containsKey(index)) {
      AudioManager.playSound(soundAtFrames.get(currentAnimationName).get(index));
    }
    lastIndex = index;
  }
}


