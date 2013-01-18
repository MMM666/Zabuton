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
	
	private static int iconIndex;
	public static Item zabuton;
	public static int uniqueID;
	public static boolean isForge;
	public static boolean isMMMLib;
	public static String textureNames[] = {
		"", "", "", "",
		"", "", "", "",
		"", "", "", "",
		"", "", "", ""
	};
	public static Class classZabuton;
	public static Minecraft mc = null;



	@Override
	public String getVersion() {
		return "1.4.7-1";
	}

	@Override
	public String getName() {
		return "Zabuton";
	}

	@Override
	public void load() {
		isForge = ModLoader.isModLoaded("Forge");
		isMMMLib = ModLoader.isModLoaded("mod_MMM_MMMLib");
		try {
			// クライアントのインスタンスを獲得
			// Forge環境下でサバー側が実行するとエラーを返す。
			mc = ModLoader.getMinecraftInstance();
		} catch (Exception e) {
		} catch (Error e) {
		}
		
		iconIndex = (isForge && isMMMLib) ? 1 : ModLoader.addOverride("/gui/items.png", "/icon/zabuton_chip.png");
		zabuton = new VZN_ItemZabuton(ItemID - 256).setIconIndex(iconIndex).setItemName("Zabuton");
		uniqueID = ModLoader.getUniqueEntityId();
		Package lpackage = this.getClass().getPackage();
		String lcname = isForge ? "VZN_EntityZabuton_Forge" : "VZN_EntityZabuton";
		if (lpackage != null) {
			lcname = lpackage.getName() + "." + lcname;
		}
		try {
			classZabuton = Class.forName(lcname);
			ModLoader.registerEntityID(classZabuton, "Zabuton", uniqueID);
			ModLoader.addEntityTracker(this, classZabuton, uniqueID, 80, 3, true);
		} catch (Exception e) {
		}
		if (isForge && isMMMLib) {
			try {
				VZN_ItemZabuton.class.getMethod("setTextureFile", String.class).invoke(zabuton, "/gui/mmmforge.png");
			} catch (Exception e) {
			}
		}
		
		for (int i = 0; i < 16; i++) {
			ModLoader.addLocalization(
					(new StringBuilder()).append(zabuton.getItemName()).append(".").append(ItemDye.dyeColorNames[15 - i]).append(".name").toString(),
					(new StringBuilder()).append("Zabuton ").append(ItemDye.dyeColorNames[15 - i]).toString()
				);
			ModLoader.addLocalization(
					(new StringBuilder()).append(zabuton.getItemName()).append(".").append(ItemDye.dyeColorNames[15 - i]).append(".name").toString(),
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
		// Modloader下では独自に生成するので要らない。
		if (!isForge) return null;
		
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


	//Modloader
//    @Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		// 面倒なので独自パケット
		return new VZN_PacketZabtonSpawn((VZN_EntityZabuton)var1);
	}

}
