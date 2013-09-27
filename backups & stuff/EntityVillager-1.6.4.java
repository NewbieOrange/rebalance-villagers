// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SourceFile

package net.minecraft.server.v1_6_R3;

import java.util.*;

public class EntityVillager extends EntityAgeable
    implements IMerchant, NPC
{

    public EntityVillager(World world)
    {
        this(world, 0);
    }

    public EntityVillager(World world, int k)
    {
        super(world);
        setProfession(k);
        a(0.6F, 1.8F);
        getNavigation().b(true);
        getNavigation().a(true);
        goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(1, new PathfinderGoalAvoidPlayer(this, net/minecraft/server/v1_6_R3/EntityZombie, 8F, 0.59999999999999998D, 0.59999999999999998D));
        goalSelector.a(1, new PathfinderGoalTradeWithPlayer(this));
        goalSelector.a(1, new PathfinderGoalLookAtTradingPlayer(this));
        goalSelector.a(2, new PathfinderGoalMoveIndoors(this));
        goalSelector.a(3, new PathfinderGoalRestrictOpenDoor(this));
        goalSelector.a(4, new PathfinderGoalOpenDoor(this, true));
        goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.59999999999999998D));
        goalSelector.a(6, new PathfinderGoalMakeLove(this));
        goalSelector.a(7, new PathfinderGoalTakeFlower(this));
        goalSelector.a(8, new PathfinderGoalPlay(this, 0.32000000000000001D));
        goalSelector.a(9, new PathfinderGoalInteract(this, net/minecraft/server/v1_6_R3/EntityHuman, 3F, 1.0F));
        goalSelector.a(9, new PathfinderGoalInteract(this, net/minecraft/server/v1_6_R3/EntityVillager, 5F, 0.02F));
        goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.59999999999999998D));
        goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, net/minecraft/server/v1_6_R3/EntityInsentient, 8F));
    }

    protected void az()
    {
        super.az();
        getAttributeInstance(GenericAttributes.d).setValue(0.5D);
    }

    public boolean bf()
    {
        return true;
    }

    protected void bk()
    {
        if(--profession <= 0)
        {
            world.villages.a(MathHelper.floor(locX), MathHelper.floor(locY), MathHelper.floor(locZ));
            profession = 70 + random.nextInt(50);
            village = world.villages.getClosestVillage(MathHelper.floor(locX), MathHelper.floor(locY), MathHelper.floor(locZ), 32);
            if(village == null)
            {
                bR();
            } else
            {
                ChunkCoordinates chunkcoordinates = village.getCenter();
                b(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, (int)((float)village.getSize() * 0.6F));
                if(bz)
                {
                    bz = false;
                    village.b(5);
                }
            }
        }
        if(!bW() && bv > 0)
        {
            bv--;
            if(bv <= 0)
            {
                if(bw)
                {
                    if(bu.size() > 1)
                    {
                        Iterator iterator = bu.iterator();
                        do
                        {
                            if(!iterator.hasNext())
                                break;
                            MerchantRecipe merchantrecipe = (MerchantRecipe)iterator.next();
                            if(merchantrecipe.g())
                                merchantrecipe.a(random.nextInt(6) + random.nextInt(6) + 2);
                        } while(true);
                    }
                    q(1);
                    bw = false;
                    if(village != null && by != null)
                    {
                        world.broadcastEntityEffect(this, (byte)14);
                        village.a(by, 1);
                    }
                }
                addEffect(new MobEffect(MobEffectList.REGENERATION.id, 200, 0));
            }
        }
        super.bk();
    }

    public boolean a(EntityHuman entityhuman)
    {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();
        boolean flag = itemstack != null && itemstack.id == Item.MONSTER_EGG.id;
        if(!flag && isAlive() && !bW() && !isBaby())
        {
            if(!world.isStatic)
            {
                a_(entityhuman);
                entityhuman.openTrade(this, getCustomName());
            }
            return true;
        } else
        {
            return super.a(entityhuman);
        }
    }

    protected void a()
    {
        super.a();
        datawatcher.a(16, Integer.valueOf(0));
    }

    public void b(NBTTagCompound nbttagcompound)
    {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Profession", getProfession());
        nbttagcompound.setInt("Riches", riches);
        if(bu != null)
            nbttagcompound.setCompound("Offers", bu.a());
    }

    public void a(NBTTagCompound nbttagcompound)
    {
        super.a(nbttagcompound);
        setProfession(nbttagcompound.getInt("Profession"));
        riches = nbttagcompound.getInt("Riches");
        if(nbttagcompound.hasKey("Offers"))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Offers");
            bu = new MerchantRecipeList(nbttagcompound1);
        }
    }

    protected boolean isTypeNotPersistent()
    {
        return false;
    }

    protected String r()
    {
        if(bW())
            return "mob.villager.haggle";
        else
            return "mob.villager.idle";
    }

    protected String aO()
    {
        return "mob.villager.hit";
    }

    protected String aP()
    {
        return "mob.villager.death";
    }

    public void setProfession(int k)
    {
        datawatcher.watch(16, Integer.valueOf(k));
    }

    public int getProfession()
    {
        return datawatcher.getInt(16);
    }

    public boolean bU()
    {
        return br;
    }

    public void i(boolean flag)
    {
        br = flag;
    }

    public void j(boolean flag)
    {
        bs = flag;
    }

    public boolean bV()
    {
        return bs;
    }

    public void b(EntityLiving entityliving)
    {
        super.b(entityliving);
        if(village != null && entityliving != null)
        {
            village.a(entityliving);
            if(entityliving instanceof EntityHuman)
            {
                byte byte0 = -1;
                if(isBaby())
                    byte0 = -3;
                village.a(((EntityHuman)entityliving).getName(), byte0);
                if(isAlive())
                    world.broadcastEntityEffect(this, (byte)13);
            }
        }
    }

    public void die(DamageSource damagesource)
    {
        if(village != null)
        {
            Entity entity = damagesource.getEntity();
            if(entity != null)
            {
                if(entity instanceof EntityHuman)
                    village.a(((EntityHuman)entity).getName(), -2);
                else
                if(entity instanceof IMonster)
                    village.h();
            } else
            if(entity == null)
            {
                EntityHuman entityhuman = world.findNearbyPlayer(this, 16D);
                if(entityhuman != null)
                    village.h();
            }
        }
        super.die(damagesource);
    }

    public void a_(EntityHuman entityhuman)
    {
        tradingPlayer = entityhuman;
    }

    public EntityHuman m_()
    {
        return tradingPlayer;
    }

    public boolean bW()
    {
        return tradingPlayer != null;
    }

    public void a(MerchantRecipe merchantrecipe)
    {
        merchantrecipe.f();
        a_ = -o();
        makeSound("mob.villager.yes", ba(), bb());
        if(merchantrecipe.a((MerchantRecipe)bu.get(bu.size() - 1)))
        {
            bv = 40;
            bw = true;
            if(tradingPlayer != null)
                by = tradingPlayer.getName();
            else
                by = null;
        }
        if(merchantrecipe.getBuyItem1().id == Item.EMERALD.id)
            riches += merchantrecipe.getBuyItem1().count;
    }

    public void a_(ItemStack itemstack)
    {
        if(!world.isStatic && a_ > -o() + 20)
        {
            a_ = -o();
            if(itemstack != null)
                makeSound("mob.villager.yes", ba(), bb());
            else
                makeSound("mob.villager.no", ba(), bb());
        }
    }

    public MerchantRecipeList getOffers(EntityHuman entityhuman)
    {
        if(bu == null)
            q(1);
        return bu;
    }

    private float p(float f)
    {
        float f1 = f + bA;
        if(f1 > 0.9F)
            return 0.9F - (f1 - 0.9F);
        else
            return f1;
    }

    private void q(int k)
    {
        if(bu != null)
            bA = MathHelper.c(bu.size()) * 0.2F;
        else
            bA = 0.0F;
        MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        switch(getProfession())
        {
        case 0: // '\0'
            a(merchantrecipelist, Item.WHEAT.id, random, p(0.9F));
            a(merchantrecipelist, Block.WOOL.id, random, p(0.5F));
            a(merchantrecipelist, Item.RAW_CHICKEN.id, random, p(0.5F));
            a(merchantrecipelist, Item.COOKED_FISH.id, random, p(0.4F));
            b(merchantrecipelist, Item.BREAD.id, random, p(0.9F));
            b(merchantrecipelist, Item.MELON.id, random, p(0.3F));
            b(merchantrecipelist, Item.APPLE.id, random, p(0.3F));
            b(merchantrecipelist, Item.COOKIE.id, random, p(0.3F));
            b(merchantrecipelist, Item.SHEARS.id, random, p(0.3F));
            b(merchantrecipelist, Item.FLINT_AND_STEEL.id, random, p(0.3F));
            b(merchantrecipelist, Item.COOKED_CHICKEN.id, random, p(0.3F));
            b(merchantrecipelist, Item.ARROW.id, random, p(0.5F));
            if(random.nextFloat() < p(0.5F))
                merchantrecipelist.add(new MerchantRecipe(new ItemStack(Block.GRAVEL, 10), new ItemStack(Item.EMERALD), new ItemStack(Item.FLINT.id, 4 + random.nextInt(2), 0)));
            break;

        case 4: // '\004'
            a(merchantrecipelist, Item.COAL.id, random, p(0.7F));
            a(merchantrecipelist, Item.PORK.id, random, p(0.5F));
            a(merchantrecipelist, Item.RAW_BEEF.id, random, p(0.5F));
            b(merchantrecipelist, Item.SADDLE.id, random, p(0.1F));
            b(merchantrecipelist, Item.LEATHER_CHESTPLATE.id, random, p(0.3F));
            b(merchantrecipelist, Item.LEATHER_BOOTS.id, random, p(0.3F));
            b(merchantrecipelist, Item.LEATHER_HELMET.id, random, p(0.3F));
            b(merchantrecipelist, Item.LEATHER_LEGGINGS.id, random, p(0.3F));
            b(merchantrecipelist, Item.GRILLED_PORK.id, random, p(0.3F));
            b(merchantrecipelist, Item.COOKED_BEEF.id, random, p(0.3F));
            break;

        case 3: // '\003'
            a(merchantrecipelist, Item.COAL.id, random, p(0.7F));
            a(merchantrecipelist, Item.IRON_INGOT.id, random, p(0.5F));
            a(merchantrecipelist, Item.GOLD_INGOT.id, random, p(0.5F));
            a(merchantrecipelist, Item.DIAMOND.id, random, p(0.5F));
            b(merchantrecipelist, Item.IRON_SWORD.id, random, p(0.5F));
            b(merchantrecipelist, Item.DIAMOND_SWORD.id, random, p(0.5F));
            b(merchantrecipelist, Item.IRON_AXE.id, random, p(0.3F));
            b(merchantrecipelist, Item.DIAMOND_AXE.id, random, p(0.3F));
            b(merchantrecipelist, Item.IRON_PICKAXE.id, random, p(0.5F));
            b(merchantrecipelist, Item.DIAMOND_PICKAXE.id, random, p(0.5F));
            b(merchantrecipelist, Item.IRON_SPADE.id, random, p(0.2F));
            b(merchantrecipelist, Item.DIAMOND_SPADE.id, random, p(0.2F));
            b(merchantrecipelist, Item.IRON_HOE.id, random, p(0.2F));
            b(merchantrecipelist, Item.DIAMOND_HOE.id, random, p(0.2F));
            b(merchantrecipelist, Item.IRON_BOOTS.id, random, p(0.2F));
            b(merchantrecipelist, Item.DIAMOND_BOOTS.id, random, p(0.2F));
            b(merchantrecipelist, Item.IRON_HELMET.id, random, p(0.2F));
            b(merchantrecipelist, Item.DIAMOND_HELMET.id, random, p(0.2F));
            b(merchantrecipelist, Item.IRON_CHESTPLATE.id, random, p(0.2F));
            b(merchantrecipelist, Item.DIAMOND_CHESTPLATE.id, random, p(0.2F));
            b(merchantrecipelist, Item.IRON_LEGGINGS.id, random, p(0.2F));
            b(merchantrecipelist, Item.DIAMOND_LEGGINGS.id, random, p(0.2F));
            b(merchantrecipelist, Item.CHAINMAIL_BOOTS.id, random, p(0.1F));
            b(merchantrecipelist, Item.CHAINMAIL_HELMET.id, random, p(0.1F));
            b(merchantrecipelist, Item.CHAINMAIL_CHESTPLATE.id, random, p(0.1F));
            b(merchantrecipelist, Item.CHAINMAIL_LEGGINGS.id, random, p(0.1F));
            break;

        case 1: // '\001'
            a(merchantrecipelist, Item.PAPER.id, random, p(0.8F));
            a(merchantrecipelist, Item.BOOK.id, random, p(0.8F));
            a(merchantrecipelist, Item.WRITTEN_BOOK.id, random, p(0.3F));
            b(merchantrecipelist, Block.BOOKSHELF.id, random, p(0.8F));
            b(merchantrecipelist, Block.GLASS.id, random, p(0.2F));
            b(merchantrecipelist, Item.COMPASS.id, random, p(0.2F));
            b(merchantrecipelist, Item.WATCH.id, random, p(0.2F));
            if(random.nextFloat() < p(0.07F))
            {
                Enchantment enchantment = Enchantment.c[random.nextInt(Enchantment.c.length)];
                int i1 = MathHelper.nextInt(random, enchantment.getStartLevel(), enchantment.getMaxLevel());
                ItemStack itemstack = Item.ENCHANTED_BOOK.a(new EnchantmentInstance(enchantment, i1));
                int k1 = 2 + random.nextInt(5 + i1 * 10) + 3 * i1;
                merchantrecipelist.add(new MerchantRecipe(new ItemStack(Item.BOOK), new ItemStack(Item.EMERALD, k1), itemstack));
            }
            break;

        case 2: // '\002'
            b(merchantrecipelist, Item.EYE_OF_ENDER.id, random, p(0.3F));
            b(merchantrecipelist, Item.EXP_BOTTLE.id, random, p(0.2F));
            b(merchantrecipelist, Item.REDSTONE.id, random, p(0.4F));
            b(merchantrecipelist, Block.GLOWSTONE.id, random, p(0.3F));
            int ai[] = {
                Item.IRON_SWORD.id, Item.DIAMOND_SWORD.id, Item.IRON_CHESTPLATE.id, Item.DIAMOND_CHESTPLATE.id, Item.IRON_AXE.id, Item.DIAMOND_AXE.id, Item.IRON_PICKAXE.id, Item.DIAMOND_PICKAXE.id
            };
            int ai1[] = ai;
            int j1 = ai1.length;
            for(int l1 = 0; l1 < j1; l1++)
            {
                int i2 = ai1[l1];
                if(random.nextFloat() < p(0.05F))
                    merchantrecipelist.add(new MerchantRecipe(new ItemStack(i2, 1, 0), new ItemStack(Item.EMERALD, 2 + random.nextInt(3), 0), EnchantmentManager.a(random, new ItemStack(i2, 1, 0), 5 + random.nextInt(15))));
            }

            break;
        }
        if(merchantrecipelist.isEmpty())
            a(merchantrecipelist, Item.GOLD_INGOT.id, random, 1.0F);
        Collections.shuffle(merchantrecipelist);
        if(bu == null)
            bu = new MerchantRecipeList();
        for(int l = 0; l < k && l < merchantrecipelist.size(); l++)
            bu.a((MerchantRecipe)merchantrecipelist.get(l));

    }

    private static void a(MerchantRecipeList merchantrecipelist, int k, Random random, float f)
    {
        if(random.nextFloat() < f)
            merchantrecipelist.add(new MerchantRecipe(a(k, random), Item.EMERALD));
    }

    private static ItemStack a(int k, Random random)
    {
        return new ItemStack(k, b(k, random), 0);
    }

    private static int b(int k, Random random)
    {
        Tuple tuple = (Tuple)bB.get(Integer.valueOf(k));
        if(tuple == null)
            return 1;
        if(((Integer)tuple.a()).intValue() >= ((Integer)tuple.b()).intValue())
            return ((Integer)tuple.a()).intValue();
        else
            return ((Integer)tuple.a()).intValue() + random.nextInt(((Integer)tuple.b()).intValue() - ((Integer)tuple.a()).intValue());
    }

    private static void b(MerchantRecipeList merchantrecipelist, int k, Random random, float f)
    {
        if(random.nextFloat() < f)
        {
            int l = c(k, random);
            ItemStack itemstack;
            ItemStack itemstack1;
            if(l < 0)
            {
                itemstack = new ItemStack(Item.EMERALD.id, 1, 0);
                itemstack1 = new ItemStack(k, -l, 0);
            } else
            {
                itemstack = new ItemStack(Item.EMERALD.id, l, 0);
                itemstack1 = new ItemStack(k, 1, 0);
            }
            merchantrecipelist.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    private static int c(int k, Random random)
    {
        Tuple tuple = (Tuple)bC.get(Integer.valueOf(k));
        if(tuple == null)
            return 1;
        if(((Integer)tuple.a()).intValue() >= ((Integer)tuple.b()).intValue())
            return ((Integer)tuple.a()).intValue();
        else
            return ((Integer)tuple.a()).intValue() + random.nextInt(((Integer)tuple.b()).intValue() - ((Integer)tuple.a()).intValue());
    }

    public GroupDataEntity a(GroupDataEntity groupdataentity)
    {
        groupdataentity = super.a(groupdataentity);
        setProfession(world.random.nextInt(5));
        return groupdataentity;
    }

    public void bX()
    {
        bz = true;
    }

    public EntityVillager b(EntityAgeable entityageable)
    {
        EntityVillager entityvillager = new EntityVillager(world);
        entityvillager.a(((GroupDataEntity) (null)));
        return entityvillager;
    }

    public boolean bG()
    {
        return false;
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return b(entityageable);
    }

    private int profession;
    private boolean br;
    private boolean bs;
    Village village;
    private EntityHuman tradingPlayer;
    private MerchantRecipeList bu;
    private int bv;
    private boolean bw;
    private int riches;
    private String by;
    private boolean bz;
    private float bA;
    private static final Map bB;
    private static final Map bC;

    static 
    {
        bB = new HashMap();
        bC = new HashMap();
        bB.put(Integer.valueOf(Item.COAL.id), new Tuple(Integer.valueOf(16), Integer.valueOf(24)));
        bB.put(Integer.valueOf(Item.IRON_INGOT.id), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
        bB.put(Integer.valueOf(Item.GOLD_INGOT.id), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
        bB.put(Integer.valueOf(Item.DIAMOND.id), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        bB.put(Integer.valueOf(Item.PAPER.id), new Tuple(Integer.valueOf(24), Integer.valueOf(36)));
        bB.put(Integer.valueOf(Item.BOOK.id), new Tuple(Integer.valueOf(11), Integer.valueOf(13)));
        bB.put(Integer.valueOf(Item.WRITTEN_BOOK.id), new Tuple(Integer.valueOf(1), Integer.valueOf(1)));
        bB.put(Integer.valueOf(Item.ENDER_PEARL.id), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
        bB.put(Integer.valueOf(Item.EYE_OF_ENDER.id), new Tuple(Integer.valueOf(2), Integer.valueOf(3)));
        bB.put(Integer.valueOf(Item.PORK.id), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
        bB.put(Integer.valueOf(Item.RAW_BEEF.id), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
        bB.put(Integer.valueOf(Item.RAW_CHICKEN.id), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
        bB.put(Integer.valueOf(Item.COOKED_FISH.id), new Tuple(Integer.valueOf(9), Integer.valueOf(13)));
        bB.put(Integer.valueOf(Item.SEEDS.id), new Tuple(Integer.valueOf(34), Integer.valueOf(48)));
        bB.put(Integer.valueOf(Item.MELON_SEEDS.id), new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
        bB.put(Integer.valueOf(Item.PUMPKIN_SEEDS.id), new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
        bB.put(Integer.valueOf(Item.WHEAT.id), new Tuple(Integer.valueOf(18), Integer.valueOf(22)));
        bB.put(Integer.valueOf(Block.WOOL.id), new Tuple(Integer.valueOf(14), Integer.valueOf(22)));
        bB.put(Integer.valueOf(Item.ROTTEN_FLESH.id), new Tuple(Integer.valueOf(36), Integer.valueOf(64)));
        bC.put(Integer.valueOf(Item.FLINT_AND_STEEL.id), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
        bC.put(Integer.valueOf(Item.SHEARS.id), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
        bC.put(Integer.valueOf(Item.IRON_SWORD.id), new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
        bC.put(Integer.valueOf(Item.DIAMOND_SWORD.id), new Tuple(Integer.valueOf(12), Integer.valueOf(14)));
        bC.put(Integer.valueOf(Item.IRON_AXE.id), new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
        bC.put(Integer.valueOf(Item.DIAMOND_AXE.id), new Tuple(Integer.valueOf(9), Integer.valueOf(12)));
        bC.put(Integer.valueOf(Item.IRON_PICKAXE.id), new Tuple(Integer.valueOf(7), Integer.valueOf(9)));
        bC.put(Integer.valueOf(Item.DIAMOND_PICKAXE.id), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
        bC.put(Integer.valueOf(Item.IRON_SPADE.id), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        bC.put(Integer.valueOf(Item.DIAMOND_SPADE.id), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
        bC.put(Integer.valueOf(Item.IRON_HOE.id), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        bC.put(Integer.valueOf(Item.DIAMOND_HOE.id), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
        bC.put(Integer.valueOf(Item.IRON_BOOTS.id), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        bC.put(Integer.valueOf(Item.DIAMOND_BOOTS.id), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
        bC.put(Integer.valueOf(Item.IRON_HELMET.id), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
        bC.put(Integer.valueOf(Item.DIAMOND_HELMET.id), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
        bC.put(Integer.valueOf(Item.IRON_CHESTPLATE.id), new Tuple(Integer.valueOf(10), Integer.valueOf(14)));
        bC.put(Integer.valueOf(Item.DIAMOND_CHESTPLATE.id), new Tuple(Integer.valueOf(16), Integer.valueOf(19)));
        bC.put(Integer.valueOf(Item.IRON_LEGGINGS.id), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
        bC.put(Integer.valueOf(Item.DIAMOND_LEGGINGS.id), new Tuple(Integer.valueOf(11), Integer.valueOf(14)));
        bC.put(Integer.valueOf(Item.CHAINMAIL_BOOTS.id), new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
        bC.put(Integer.valueOf(Item.CHAINMAIL_HELMET.id), new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
        bC.put(Integer.valueOf(Item.CHAINMAIL_CHESTPLATE.id), new Tuple(Integer.valueOf(11), Integer.valueOf(15)));
        bC.put(Integer.valueOf(Item.CHAINMAIL_LEGGINGS.id), new Tuple(Integer.valueOf(9), Integer.valueOf(11)));
        bC.put(Integer.valueOf(Item.BREAD.id), new Tuple(Integer.valueOf(-4), Integer.valueOf(-2)));
        bC.put(Integer.valueOf(Item.MELON.id), new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
        bC.put(Integer.valueOf(Item.APPLE.id), new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
        bC.put(Integer.valueOf(Item.COOKIE.id), new Tuple(Integer.valueOf(-10), Integer.valueOf(-7)));
        bC.put(Integer.valueOf(Block.GLASS.id), new Tuple(Integer.valueOf(-5), Integer.valueOf(-3)));
        bC.put(Integer.valueOf(Block.BOOKSHELF.id), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
        bC.put(Integer.valueOf(Item.LEATHER_CHESTPLATE.id), new Tuple(Integer.valueOf(4), Integer.valueOf(5)));
        bC.put(Integer.valueOf(Item.LEATHER_BOOTS.id), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
        bC.put(Integer.valueOf(Item.LEATHER_HELMET.id), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
        bC.put(Integer.valueOf(Item.LEATHER_LEGGINGS.id), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
        bC.put(Integer.valueOf(Item.SADDLE.id), new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
        bC.put(Integer.valueOf(Item.EXP_BOTTLE.id), new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
        bC.put(Integer.valueOf(Item.REDSTONE.id), new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
        bC.put(Integer.valueOf(Item.COMPASS.id), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
        bC.put(Integer.valueOf(Item.WATCH.id), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
        bC.put(Integer.valueOf(Block.GLOWSTONE.id), new Tuple(Integer.valueOf(-3), Integer.valueOf(-1)));
        bC.put(Integer.valueOf(Item.GRILLED_PORK.id), new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
        bC.put(Integer.valueOf(Item.COOKED_BEEF.id), new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
        bC.put(Integer.valueOf(Item.COOKED_CHICKEN.id), new Tuple(Integer.valueOf(-8), Integer.valueOf(-6)));
        bC.put(Integer.valueOf(Item.EYE_OF_ENDER.id), new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
        bC.put(Integer.valueOf(Item.ARROW.id), new Tuple(Integer.valueOf(-12), Integer.valueOf(-8)));
    }
}
