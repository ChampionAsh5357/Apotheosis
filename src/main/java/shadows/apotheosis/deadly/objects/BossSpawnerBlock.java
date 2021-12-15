package shadows.apotheosis.deadly.objects;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import shadows.apotheosis.ApotheosisObjects;
import shadows.apotheosis.deadly.gen.BossItem;
import shadows.apotheosis.deadly.reload.BossItemManager;

public class BossSpawnerBlock extends Block {

	public BossSpawnerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new BossSpawnerTile();
	}

	public static class BossSpawnerTile extends BlockEntity implements TickableBlockEntity {

		protected BossItem item;
		protected int ticks = 0;

		public BossSpawnerTile() {
			super(ApotheosisObjects.BOSS_SPAWN_TILE);
		}

		@Override
		public void tick() {
			if (this.level.isClientSide) return;
			if (this.ticks++ % 40 == 0 && this.level.getEntities(EntityType.PLAYER, new AABB(this.worldPosition).inflate(8, 8, 8), EntitySelector.NO_SPECTATORS).stream().anyMatch(p -> !p.isCreative())) {
				this.level.setBlockAndUpdate(this.worldPosition, Blocks.AIR.defaultBlockState());
				BlockPos pos = this.worldPosition;
				if (this.item != null) {
					Mob entity = this.item.createBoss((ServerLevel) this.level, pos, this.level.getRandom());
					entity.setPersistenceRequired();
					this.level.addFreshEntity(entity);
				}
			}
		}

		public void setBossItem(BossItem item) {
			this.item = item;
		}

		@Override
		public CompoundTag save(CompoundTag tag) {
			tag.putString("boss_item", this.item.getId().toString());
			return super.save(tag);
		}

		@Override
		public void load(BlockState state, CompoundTag tag) {
			this.item = BossItemManager.INSTANCE.getById(new ResourceLocation(tag.getString("boss_item")));
			super.load(state, tag);
		}

	}

}
