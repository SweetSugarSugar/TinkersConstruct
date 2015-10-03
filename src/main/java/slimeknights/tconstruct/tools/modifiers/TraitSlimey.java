package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitSlimey extends AbstractTrait {

  private static float chance = 0.01f;

  protected final Class<? extends EntitySlime> slime;

  public TraitSlimey(Class<? extends EntitySlime> slime) {
    super("slimey", EnumChatFormatting.GREEN);
    this.slime = slime;
  }

  @Override
  public void afterBlockBreak(ItemStack tool, World world, Block block, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
    if(wasEffective && !world.isRemote && random.nextFloat() < chance) {
      spawnSlime(player, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world);
    }
  }

  @Override
  public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
    if(!target.isEntityAlive() && !target.worldObj.isRemote && random.nextFloat() < chance) {
      spawnSlime(player, target.posX, target.posY, target.posZ, target.worldObj);
    }
  }

  protected void spawnSlime(EntityLivingBase player, double x, double y, double z, World world) {
    try {
      EntitySlime entity = slime.getConstructor(World.class).newInstance(world);
      //entity.setSlimeSize(1);
      entity.setPosition(x,y,z);
      world.spawnEntityInWorld(entity);
      entity.setLastAttacker(player);
      entity.playLivingSound();
    } catch(InstantiationException e) {
      e.printStackTrace();
    } catch(IllegalAccessException e) {
      e.printStackTrace();
    } catch(InvocationTargetException e) {
      e.printStackTrace();
    } catch(NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

}