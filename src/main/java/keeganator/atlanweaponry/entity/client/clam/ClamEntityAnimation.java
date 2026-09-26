package keeganator.atlanweaponry.entity.client.clam;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

import java.util.List;
import java.util.Map;

public class ClamEntityAnimation {
    public static final AnimationDefinition IDLE = new AnimationDefinition(3.0F, true, Map.of(
            "Top Shell", List.of(
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.5F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)),
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)),
                    new AnimationChannel(AnimationChannel.Targets.SCALE,
                            new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR))
            )
    ));

    public static final AnimationDefinition OPEN = new AnimationDefinition(7.5F, false, Map.of(
            "Top Shell", List.of(
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.75F, KeyframeAnimations.degreeVec(120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(6.0F, KeyframeAnimations.degreeVec(120.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(6.5F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(6.6667F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(6.75F, KeyframeAnimations.degreeVec(-14.16F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(7.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)),
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)),
                    new AnimationChannel(AnimationChannel.Targets.SCALE,
                            new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR))
            )
    ));
}