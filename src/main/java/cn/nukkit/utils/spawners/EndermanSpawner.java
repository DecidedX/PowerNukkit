package cn.nukkit.utils.spawners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.AbstractEntitySpawner;
import cn.nukkit.utils.EntityUtils;
import cn.nukkit.utils.Spawner;
import cn.nukkit.utils.SpawnResult;

public class EndermanSpawner extends AbstractEntitySpawner {

    public EndermanSpawner(Spawner spawnTask) {
        super(spawnTask);
    }

    public SpawnResult spawn(Player player, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        if (EntityUtils.rand(0, 3) > 0 && !level.getName().equals("end")) {
            return SpawnResult.SPAWN_DENIED;
        }

        final int time = level.getTime() % Level.TIME_FULL;
        final int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);

        if ((pos.y > 255 || (level.getName().equals("nether") && pos.y > 127)) || pos.y < 1 || blockId == Block.AIR) {
            result = SpawnResult.POSITION_MISMATCH;
        } else if (Block.transparent[blockId]) {
            result = SpawnResult.WRONG_BLOCK;
        } else if (level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z) > 7 && !level.getName().equals("nether") && !level.getName().equals("end")) {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if ((time > 13184 && time < 22800) || level.getName().equals("nether") || level.getName().equals("end")) {
            this.spawnTask.createEntity("Enderman", pos.add(0, 1, 0));
        }

        return result;
    }

    @Override
    public final int getEntityNetworkId() {
        return EntityEnderman.NETWORK_ID;
    }
}
