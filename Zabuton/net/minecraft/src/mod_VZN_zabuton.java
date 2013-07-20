package net.minecraft.src;

import java.util.Map;

public class mod_VZN_zabuton extends BaseMod {
	
	@MLProp(info="Zabuton's ItemID.(shiftedIndex = ItemID)")
	public static int ItemID = 22202;
	@MLProp
	public static boolean isDebugMessage = true;
	
	public static Item zabuton;
	public static Class classZabuton;



	public static void Debug(String pText, Object... pData) {
		// �f�o�b�O���b�Z�[�W
		if (isDebugMessage) {
			System.out.println(String.format("Zabuton-" + pText, pData));
		}
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
	public String getVersion() {
		return "1.6.2-2";
	}

	@Override
	public void load() {
		// MMMLib��Revision�`�F�b�N
		MMM_Helper.checkRevision("3");
		
		zabuton = new VZN_ItemZabuton(ItemID - 256).setUnlocalizedName("zabuton").func_111206_d("zabuton");
		classZabuton = MMM_Helper.getForgeClass(this, "VZN_EntityZabuton");
		MMM_Helper.registerEntity(classZabuton, "Zabuton", 0, this, 80, 3, true);
		
		for (int i = 0; i < 16; i++) {
			ModLoader.addLocalization(
					(new StringBuilder()).append(zabuton.getUnlocalizedName()).append(".").append(ItemDye.dyeColorNames[15 - i]).append(".name").toString(),
					(new StringBuilder()).append("Zabuton ").append(ItemDye.dyeColorNames[15 - i]).toString()
				);
			ModLoader.addLocalization(
					(new StringBuilder()).append(zabuton.getUnlocalizedName()).append(".").append(ItemDye.dyeColorNames[15 - i]).append(".name").toString(),
					"ja_JP",
					(new StringBuilder()).append("���z�c ").append(VZN_ItemZabuton.colorNamesJP[15 - i]).toString()
				);
			ModLoader.addRecipe(new ItemStack(zabuton, 1, 15 - i), new Object[] {
				"s ", "##", 
				Character.valueOf('s'), Item.silk,
				Character.valueOf('#'), new ItemStack(Block.cloth, 1, i)
			});
		}
		
		ModLoader.addDispenserBehavior(zabuton, new VZN_BehaviorZabutonDispense());
	}

	@Override
	public void addRenderer(Map map) {
		// Render��ǉ�
		map.put(VZN_EntityZabuton.class, new VZN_RenderZabuton());
	}

	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		//Modloader
		// �ʓ|�Ȃ̂œƎ��p�P�b�g
		return new VZN_PacketZabtonSpawn((VZN_EntityZabuton)var1);
	}

}
