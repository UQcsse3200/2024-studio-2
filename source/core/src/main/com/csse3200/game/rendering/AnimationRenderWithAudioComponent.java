package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
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
