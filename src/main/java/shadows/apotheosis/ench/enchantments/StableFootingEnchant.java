package shadows.apotheosis.ench.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class StableFootingEnchant extends Enchantment {

	public StableFootingEnchant() {
		super(Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
	}

	@Override
	public int getMinCost(int level) {
		return 40;
	}

	@Override
	public int getMaxCost(int level) {
		return 100;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

}