package dev.monarkhes.myrontest;

import dev.monarkhēs.myron.api.Myron;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;

import java.util.Random;

public class TorusBlockEntityRenderer extends BlockEntityRenderer<TorusBlockEntity> {
    private static final Identifier MODEL = new Identifier("myron", "models/torus.obj");

    public TorusBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TorusBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Random random = new Random(entity.getPos().asLong());
        int r = random.nextInt(100);

        BakedModel model = Myron.getModel(MODEL);

        if (model != null) {
            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getSolid());

            matrices.push();

            float time = (entity.getWorld() == null ? 0 : entity.getWorld().getTime()) + tickDelta + r;

            matrices.translate(0.5F, 0.5F, 0.5F);

            float scale = (float) (0.9 + 0.2 * Math.sin(time * 0.025));
            matrices.scale(scale, scale, scale);

            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(2 * (time)));

            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(0.8F * time));

            matrices.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(1.4F * time));

            matrices.translate(-0.5F, -0.5F, -0.5F);

            matrices.translate(0, 0.125 * Math.sin(time * 0.1), 0);

            MatrixStack.Entry entry = matrices.peek();

            model.getQuads(null, null, entity.getWorld().random).forEach(quad -> {
                consumer.quad(entry, quad, 1F, 1F, 1F, light, overlay);
            });

            matrices.pop();
        }
    }
}