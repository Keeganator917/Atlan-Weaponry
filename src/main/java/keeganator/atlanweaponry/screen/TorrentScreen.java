package keeganator.atlanweaponry.screen;

import keeganator.atlanweaponry.util.network.TorrentPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class TorrentScreen extends Screen {

    private static final Identifier BAR_TEXTURE =
            Identifier.fromNamespaceAndPath("atlanweaponry", "textures/gui/torrent_bar.png");

    private static final int BAR_W = 64;
    private static final int BAR_H = 32;


    private static final int TEX_W = 64;
    private static final int TEX_H = 32;

    private static final float GREEN_START = 0.40f;
    private static final float GREEN_END   = 0.58f;

    private static final long SWEEP_DURATION_MS = 1000L;

    private long openTime;
    private boolean resolved = false;
    private boolean success = false;
    private long resolvedTime = 0L;
    private static final long RESULT_DISPLAY_MS = 600L;

    private boolean waitingForRelease = true;
    private boolean wasPressed = false;

    public TorrentScreen() {
        super(Component.translatable("screen.atlanweaponry.torrent"));
    }

    @Override
    protected void init() {
        super.init();

        wasPressed = Minecraft.getInstance().options.keyUse.isDown();
        openTime = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        boolean pressed = GLFW.glfwGetMouseButton(
                Minecraft.getInstance().getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT
        ) == GLFW.GLFW_PRESS;

        if (waitingForRelease) {
            if (!pressed) {
                waitingForRelease = false;
            }
        } else {
            if (!resolved && pressed && !wasPressed) {
                resolve(System.currentTimeMillis());
            }
        }

        wasPressed = pressed;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        long now = System.currentTimeMillis();

        if (!resolved && (now - openTime) > SWEEP_DURATION_MS + 200L) {
            this.onClose();
            return;
        }

        if (resolved && (now - resolvedTime) > RESULT_DISPLAY_MS) {
            this.onClose();
            return;
        }

        int barX = (this.width  - BAR_W) / 2;
        int barY = (this.height - BAR_H) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, BAR_TEXTURE, barX, barY, 0, 0, BAR_W, BAR_H, TEX_W, TEX_H);

        if (!resolved) {
            float progress = (float)((now - openTime) % SWEEP_DURATION_MS) / SWEEP_DURATION_MS;
            int markerX = barX + (int)(progress * BAR_W);

           //Draws aim thing marker
            graphics.fill(markerX, barY, markerX + 2, barY + BAR_H, 0xFFFFFFFF);
        } else {
            int flashColor = success ? 0x8800FF44 : 0x88FF2222;
            graphics.fill(barX, barY, barX + BAR_W, barY + BAR_H, flashColor);

            String label = success ? "SUCCESS" : "MISSED";
            int textColor = success ? 0x00FF88 : 0xFF4444;
            graphics.text(this.font, label, this.width / 2 - this.font.width(label) / 2, barY - 14, textColor, false);
        }

        super.extractBackground(graphics, mouseX, mouseY, a);
    }

    private void resolve(long now) {
        float progress = (float)((now - openTime) % SWEEP_DURATION_MS) / SWEEP_DURATION_MS;

        resolved = true;
        resolvedTime = now;

        success = (progress >= GREEN_START && progress <= GREEN_END);

        if (success) {
            ClientPlayNetworking.send(new TorrentPayload());
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
