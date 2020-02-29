package cn.nukkit.level.generator.standard.gen.decorator;

import cn.nukkit.level.chunk.IChunk;
import cn.nukkit.level.generator.standard.misc.AbstractGenerationPass;
import cn.nukkit.level.generator.standard.misc.filter.BlockFilter;
import cn.nukkit.level.generator.standard.misc.layer.BlockLayer;
import cn.nukkit.level.generator.standard.misc.layer.BlockLayersDeserializer;
import cn.nukkit.utils.Identifier;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.daporkchop.lib.random.PRandom;

/**
 * Replaces all blocks of a given type with a certain pattern of replacement blocks from the top down.
 *
 * @author DaPorkchop_
 */
@JsonDeserialize
public final class SurfaceDecorator extends AbstractGenerationPass implements Decorator {
    public static final Identifier ID = Identifier.fromString("nukkitx:surface");

    @JsonProperty(required = true)
    private BlockFilter  target;
    @JsonProperty(required = true)
    @JsonDeserialize(using = BlockLayersDeserializer.class)
    private BlockLayer[] layers;

    @Override
    public void decorate(IChunk chunk, PRandom random, int x, int z) {
        for (int prevId = 0, y = 255; y >= 0; y--) {
            int id = chunk.getBlockRuntimeIdUnsafe(x, y, z, 0);
            if (prevId == 0 && this.target.test(id)) {
                LAYERS:
                for (BlockLayer layer : this.layers) {
                    for (int replaceId = layer.runtimeId(), i = layer.size(random) - 1; y >= 0 && i >= 0; i--) {
                        chunk.setBlockRuntimeIdUnsafe(x, y, z, 0, replaceId);
                        if (!this.target.test(id = chunk.getBlockRuntimeIdUnsafe(x, --y, z, 0))) {
                            break LAYERS;
                        }
                    }
                }
            }
            prevId = id;
        }
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}