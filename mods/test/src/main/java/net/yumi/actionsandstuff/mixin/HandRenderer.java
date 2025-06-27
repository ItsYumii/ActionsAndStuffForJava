package net.yumi.actionsandstuff.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.Arm;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.yumi.actionsandstuff.ActionsStuffForJava;
import net.yumi.actionsandstuff.anim.ActiveAnimation;
import net.yumi.actionsandstuff.anim.AnimationManager;
import net.yumi.actionsandstuff.anim.EasingFunctions;
import net.yumi.actionsandstuff.event.KeyInputHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(HeldItemRenderer.class)
public class HandRenderer {

//    private double constStand = -0.0784000015258789;
    private final double[] settingsRot = {0, 0, 0};
    private final double[] settingsMove = {0, 0, 0};

    private final float[] rotOffset = {0, 0, 0};
    private final float[] moveOffset = {0, 0, 0};

    private ItemStack mainItem = ItemStack.EMPTY;

    public boolean wasPlayerOnGround = true;

    private final float[] idle = {0, 0, 0};
    float fallingOffsetGoal = 0.0F;
    float fallingOffset = 0.0F;

    long lastTime = System.nanoTime();
    public double deltaTime;
    MinecraftClient client = MinecraftClient.getInstance();
    public double deltaSum;
    private final List<ActiveAnimation> activeAnimations = new ArrayList<>();
    TagKey<Item> PLACEABLE_BLOCKS = TagKey.of(RegistryKeys.ITEM, new Identifier(ActionsStuffForJava.MOD_ID, "placeable_blocks"));

    @Inject(
            method = "renderFirstPersonItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderFirstPersonItem(AbstractClientPlayerEntity player, float pitch, float handSwingProgress, Hand hand, float equipProgress, ItemStack stack, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        // USUWA RENCEEEEE!
        ci.cancel();

        renderHands(player, pitch, handSwingProgress, hand, equipProgress, stack, tickDelta, matrices, vertexConsumers, light);
    }

    private void renderHands(AbstractClientPlayerEntity player, float pitch, float handSwingProgress, Hand hand, float equipProgress, ItemStack stack, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        // declaring all necessary stuff 4 later
        long now = System.nanoTime();
        deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;
        deltaSum += deltaTime;

//        System.out.printf("%.20f%n", deltaTime);

        float side = (hand == Hand.MAIN_HAND) ?  1.0F : -1.0F;
        float mainHand = player.getMainArm() == Arm.RIGHT ?  1.0F : -1.0F;
        boolean isMainHand = (hand == Hand.MAIN_HAND);
        boolean isMainHandRight = (player.getMainArm() == Arm.RIGHT);

        PlayerEntityRenderer renderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player);

        // ustawiamy raczki
        matrices.push();


//        matrices.translate((0.38F + moveOffset[0]) * side * mainHand,
//                           -1.20F + moveOffset[1] + idle[1] + fallingOffset,
//                           -0.82F + moveOffset[2]);

        matrices.scale(0.95f, 0.95f, 0.95f);
        if (mainItem.isIn(PLACEABLE_BLOCKS)) {
//            matrices.translate((0.22F + settingsMove[0] + moveOffset[0]) * side * mainHand, (-0.84F + settingsMove[1] + moveOffset[1] + 0.3F * fallingOffset), (-0.09F + settingsMove[2] + moveOffset[2] - 0.08 * fallingOffset));
//            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((-86f + (int) settingsRot[0] + rotOffset[0] + 10 * fallingOffset)));
//            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((-1f + (int) settingsRot[2] + rotOffset[2] + 2 * fallingOffset) * side * mainHand));
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((180f + (int) settingsRot[1] + rotOffset[1]) * side * mainHand));
            matrices.translate((0.22F + moveOffset[0]) * side * mainHand,
                               -0.84F + moveOffset[1] + idle[1] + 0.3F * fallingOffset,
                               -0.09F + moveOffset[2] - 0.08F * fallingOffset);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((-86F + rotOffset[0] + 10F * fallingOffset)));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((-1F - idle[2] + rotOffset[2] + 2F * fallingOffset) * side * mainHand));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((180F + rotOffset[1]) * side * mainHand));
        } else {
//            matrices.translate((0.38F + settingsMove[0] + moveOffset[0]) * side * mainHand, (-1.20F + settingsMove[1] + moveOffset[1] + 0.3F * fallingOffset), (-0.82F + settingsMove[2] + moveOffset[2]));
//            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((-57f + (int) settingsRot[0] + rotOffset[0])));
//            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((-20f + (int) settingsRot[1] + rotOffset[2]) * side * mainHand));
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((111f + (int) settingsRot[2] + rotOffset[1]) * side * mainHand));
            matrices.translate((0.38F + moveOffset[0]) * side * mainHand,
                               -1.20F + moveOffset[1] + idle[1] + 0.3F * fallingOffset,
                               -0.82F + moveOffset[2]);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((-57f + rotOffset[0] + idle[2])));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((-20f + rotOffset[2]) * side * mainHand));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((111f + rotOffset[1]) * side * mainHand));
        }

        if (isMainHand && isMainHandRight) {
            renderer.renderRightArm(matrices, vertexConsumers, light, player);
        } else if (isMainHand) {
            renderer.renderLeftArm(matrices, vertexConsumers, light, player);
        }

//        if(item.isEmpty()) {
//            System.out.println("nic");
//        } else if (item.isOf(Items.DIAMOND_SWORD)) {
//            System.out.println("miecz");
//        } else {
//            System.out.println("cos innego");
//        }

//        \/\/\/\/\/ ITEMS \/\/\/\/\/

//        matrices.translate((-0.34F + settingsMove[0]) * side * mainHand, 0.72F + settingsMove[1], -0.26F + settingsMove[2]);
//        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-91F + (int) settingsRot[0]));
//        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((0F + (int) settingsRot[2]) * side * mainHand));
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((-20F + (int) settingsRot[1]) * side * mainHand));
        matrices.translate(-0.34F * side * mainHand, 0.72F, -0.26F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-91F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0F * side * mainHand));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-20F * side * mainHand));

        ItemRenderer itemRenderer = client.getItemRenderer();

        itemRenderer.renderItem(player, player.getMainHandStack(), ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, !isMainHandRight, matrices, vertexConsumers, player.getWorld(), light, OverlayTexture.DEFAULT_UV, 0);

        matrices.pop();

        updateHeldItemsss();
        handleInputs();
        animationHandler(player, equipProgress, side, mainHand);
    }

    private void handleInputs() {
        if (KeyInputHandler.rotX.isPressed() && !client.options.sneakKey.isPressed()) {
            settingsRot[0] += 10 * deltaTime;
        } else if (KeyInputHandler.rotX.isPressed()) {
            settingsRot[0] -= 10 * deltaTime;
        }
        if (KeyInputHandler.rotY.isPressed() && !client.options.sneakKey.isPressed()) {
            settingsRot[1] += 10 * deltaTime;
        } else if (KeyInputHandler.rotY.isPressed()) {
            settingsRot[1] -= 10 * deltaTime;
        }
        if (KeyInputHandler.rotZ.isPressed() && !client.options.sneakKey.isPressed()) {
            settingsRot[2] += 10 * deltaTime;
        } else if (KeyInputHandler.rotZ.isPressed()) {
            settingsRot[2] -= 10 * deltaTime;
        }

        if (KeyInputHandler.moveX.isPressed() && !client.options.sneakKey.isPressed()) {
            settingsMove[0] += 0.1 * deltaTime;
        } else if (KeyInputHandler.moveX.isPressed()) {
            settingsMove[0] -= 0.1 * deltaTime;
        }
        if (KeyInputHandler.moveY.isPressed() && !client.options.sneakKey.isPressed()) {
            settingsMove[1] += 0.1 * deltaTime;
        } else if (KeyInputHandler.moveY.isPressed()) {
            settingsMove[1] -= 0.1 * deltaTime;
        }
        if (KeyInputHandler.moveZ.isPressed() && !client.options.sneakKey.isPressed()) {
            settingsMove[2] += 0.1 * deltaTime;
        } else if (KeyInputHandler.moveZ.isPressed()) {
            settingsMove[2] -= 0.1 * deltaTime;
        }

        if (deltaSum > 1) {
            System.out.println("Rot X: " + (int) settingsRot[0] + ", Rot Z: " + (int) settingsRot[2] + ", Rot Y: " + (int) settingsRot[1]);
            System.out.println("Move X: " + String.format("%.2f", settingsMove[0]) + ", Move Y: " + String.format("%.2f", settingsMove[1]) + ", Move Y: " + String.format("%.2f", settingsMove[2]));
            deltaSum -= 1;
        }
    }

    private void animationHandler(AbstractClientPlayerEntity player, float equipProgress, float side, float mainHand) {
        idle[0] += (float) (1F * deltaTime);
        moveOffset[0] = moveOffset[1] = moveOffset[2] = 0;
        rotOffset[0] = rotOffset[1] = rotOffset[2] = 0;

        if (idle[0] > 2) {
            idle[0] -= 2;
        }
        idle[1] = MathHelper.cos(MathHelper.PI * idle[0]) / 100;
        idle[2] = MathHelper.sin(MathHelper.PI * idle[0]);

        queueAnimations(player, equipProgress, side, mainHand);

        if(!player.isOnGround()) {
            fallingOffsetGoal = (float) EasingFunctions.sigmoid(4 * -player.getVelocity().y) * 2 - 1;
        }
        if(MathHelper.abs(fallingOffsetGoal - fallingOffset) < 1E-10) {
            fallingOffsetGoal = fallingOffset;
        }

        Iterator<ActiveAnimation> iterator = activeAnimations.iterator();
        while (iterator.hasNext()) {
            ActiveAnimation anim = iterator.next();

            if (Objects.equals(anim.id, "falling")) {
                List<AnimationManager.Keyframe> layer = anim.animation.layers.get("layer1");
                if (layer != null && layer.size() >= 2) {
                    anim.updateKeyframeValue("layer1", 0, new float[]{0, fallingOffset, 0}, new float[]{0, 0, 0});
                    anim.updateKeyframeValue("layer1", 1, new float[]{0, fallingOffsetGoal, 0}, new float[]{0, 0, 0});

                    anim.update(deltaTime);

                    float yOffset = anim.getLayerPositionOffset("layer1")[1];
                    if (!Float.isNaN(yOffset)) {
                        fallingOffset = yOffset;
                    }
                }
            } else {
                for (String layer : anim.animation.layers.keySet()) {
                    float[] pos = anim.getLayerPositionOffset(layer);
                    float[] rot = anim.getLayerRotationOffset(layer);

                    moveOffset[0] += pos[0];
                    moveOffset[1] += pos[1];
                    moveOffset[2] += pos[2];
                    rotOffset[0] += rot[0];
                    rotOffset[1] += rot[1];
                    rotOffset[2] += rot[2];
                }
            }

            anim.update(deltaTime);

            if (!anim.loop && anim.isFinished()) {
                iterator.remove();
            }
        }
    }

    public void queueAnimations(AbstractClientPlayerEntity player, float equipProgress, float side, float mainHand) {
        if (mainItem.isEmpty() && equipProgress > 0 && equipProgress < 0.2) {queueAnimation("punch", false);}
        if (player.isOnGround() && !wasPlayerOnGround) {queueAnimation("land", false);fallingOffsetGoal = 0;}

        queueAnimation("falling", true);
        wasPlayerOnGround = player.isOnGround();
    }

    public void queueAnimation(String id, boolean loop) {
        AnimationManager.Animation anim = AnimationManager.getAnimation(id);
        if (anim == null) {
            System.out.println("Animation not found: " + id);
            return;
        }

        for (ActiveAnimation aa : activeAnimations) {
            if (aa.id.equals(id) && aa.loop == loop) {
                boolean canReset = aa.layerTimes.values().stream().allMatch(t -> t >= anim.lockTime);
                if (canReset) aa.reset();
                return;
            }
        }

        activeAnimations.add(new ActiveAnimation(id, anim, loop));
    }

    private void updateHeldItemsss() {
        ItemStack item = ItemStack.EMPTY;
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        assert clientPlayerEntity != null;
        item = clientPlayerEntity.getMainHandStack();

        mainItem = item;
    }
}
