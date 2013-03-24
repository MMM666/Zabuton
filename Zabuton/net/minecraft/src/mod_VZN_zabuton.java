package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;

public class mod_VZN_zabuton extends BaseMod {
	
	@MLProp(info="Zabuton's ItemID.(shiftedIndex = ItemID)")
	public static int ItemID = 22202;

	@MLProp(info="color=0's texture. Null is default.")
	public static String texture_0 = "";
	@MLProp(info="color=1's texture. Null is default.")
	public static String texture_1 = "";
	@MLProp(info="color=2's texture. Null is default.")
	public static String texture_2 = "";
	@MLProp(info="color=3's texture. Null is default.")
	public static String texture_3 = "";
	@MLProp(info="color=4's texture. Null is default.")
	public static String texture_4 = "";
	@MLProp(info="color=5's texture. Null is default.")
	public static String texture_5 = "";
	@MLProp(info="color=6's texture. Null is default.")
	public static String texture_6 = "";
	@MLProp(info="color=7's texture. Null is default.")
	public static String texture_7 = "";
	@MLProp(info="color=8's texture. Null is default.")
	public static String texture_8 = "";
	@MLProp(info="color=9's texture. Null is default.")
	public static String texture_9 = "";
	@MLProp(info="color=10's texture. Null is default.")
	public static String texture_a = "";
	@MLProp(info="color=11's texture. Null is default.")
	public static String texture_b = "";
	@MLProp(info="color=12's texture. Null is default.")
	public static String texture_c = "";
	@MLProp(info="color=13's texture. Null is default.")
	public static String texture_d = "/item/zabuton_d.png";
	@MLProp(info="color=14's texture. Null is default.")
	public static String texture_e = "";
	@MLProp(info="color=15's texture. Null is default.")
	public static String texture_f = "";
	@MLProp(info="true is enable alternativ recipe.")
	public static boolean isAlternativeRecipe = false;
	@MLProp
	public static boolean isDebugMessage = true;
	
	public static Item zabuton;
	public static String textureNames[] = {
		"", "", "", "",
		"", "", "", "",
		"", "", "", "",
		"", "", "", ""
	};
	public static Class classZabuton;



	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if (isDebugMessage) {
			System.out.println(String.format("Zabuton-" + pText, pData));
		}
	}

	@Override
	public String getVersion() {
		return "1.5.1-1";
	}

	@Override
	public String getName() {
		return "Zabuton";
	}

	@Override
	public String getPriorities() {
		return "required-after:mod_MMM_MMMLib";
	}

	@Override
	public void load() {
		int lentityid = MMM_Helper.getNextEntityID(false);
		if (lentityid == -1) {
			Debug("Break Zabuton.(can't registar EntityID.)");
		}
		
		zabuton = new VZN_ItemZabuton(ItemID - 256).setUnlocalizedName("zabuton");
		classZabuton = MMM_Helper.getForgeClass(this, "VZN_EntityZabuton");
		ModLoader.registerEntityID(classZabuton, "Zabuton", lentityid);
		ModLoader.addEntityTracker(this, classZabuton, lentityid, 80, 3, true);
		
		for (int i = 0; i < 16; i++) {
			ModLoader.addLocalization(
					(new StringBuilder()).append(zabuton.getUnlocalizedName()).append(".").append(ItemDye.dyeColorNames[15 - i]).append(".name").toString(),
					(new StringBuilder()).append("Zabuton ").append(ItemDye.dyeColorNames[15 - i]).toString()
				);
			ModLoader.addLocalization(
					(new StringBuilder()).append(zabuton.getUnlocalizedName()).append(".").append(ItemDye.dyeColorNames[15 - i]).append(".name").toString(),
					"ja_JP",
					(new StringBuilder()).append("座布団 ").append(VZN_ItemZabuton.colorNamesJP[15 - i]).toString()
				);
			if (isAlternativeRecipe) {
				ModLoader.addRecipe(new ItemStack(zabuton, 1, 15 - i), new Object[] {
					"s ", "##", 
					Character.valueOf('s'), Item.silk,
					Character.valueOf('#'), new ItemStack(Block.cloth, 1, i)
				});
			} else {
				ModLoader.addRecipe(new ItemStack(zabuton, 1, 15 - i), new Object[] {
					"##", Character.valueOf('#'), new ItemStack(Block.cloth, 1, i)
				});
			}
		}
		
		textureNames[15]	= texture_0.trim();
		textureNames[14]	= texture_1.trim();
		textureNames[13]	= texture_2.trim();
		textureNames[12]	= texture_3.trim();
		textureNames[11]	= texture_4.trim();
		textureNames[10]	= texture_5.trim();
		textureNames[9]		= texture_6.trim();
		textureNames[8]		= texture_7.trim();
		textureNames[7]		= texture_8.trim();
		textureNames[6]		= texture_9.trim();
		textureNames[5]		= texture_a.trim();
		textureNames[4]		= texture_b.trim();
		textureNames[3]		= texture_c.trim();
		textureNames[2]		= texture_d.trim();
		textureNames[1]		= texture_e.trim();
		textureNames[0]		= texture_f.trim();
		
		ModLoader.addDispenserBehavior(zabuton, new VZN_BehaviorZabutonDispense(null));
	}

	@Override
	public void addRenderer(Map map) {
		// Renderを追加
		map.put(VZN_EntityZabuton.class, new VZN_RenderZabuton());
	}

	@Override
	public Entity spawnEntity(int entityId, World world, double scaledX, double scaledY, double scaledZ) {
		// Forge
		if (!MMM_Helper.isForge) return null;
		
		try {
			Constructor<VZN_EntityZabuton> lconstructor = classZabuton.getConstructor(World.class);
			VZN_EntityZabuton lentity = lconstructor.newInstance(world);
			lentity.entityId = entityId;
			lentity.setLocationAndAngles(scaledX, scaledY, scaledZ, 0F, 0F);
			
			return lentity;
		} catch (Exception e) {
		}
		
		return null;
	}

	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		//Modloader
		// 面倒なので独自パケット
		return new VZN_PacketZabtonSpawn((VZN_EntityZabuton)var1);
	}

}
