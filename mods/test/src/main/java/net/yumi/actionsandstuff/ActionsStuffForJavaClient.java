package net.yumi.actionsandstuff;

import net.fabricmc.api.ClientModInitializer;
import net.yumi.actionsandstuff.event.KeyInputHandler;
import net.yumi.actionsandstuff.anim.AnimationManager;

public class ActionsStuffForJavaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("========== Initializing Actions & Stuff ==========");
        KeyInputHandler.register();
        AnimationManager.loadAnimations();
    }
}
