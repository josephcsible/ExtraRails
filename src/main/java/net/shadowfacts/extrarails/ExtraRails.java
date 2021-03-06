package net.shadowfacts.extrarails;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.shadowfacts.extrarails.block.ModBlocks;
import net.shadowfacts.extrarails.gui.GUIHandler;
import net.shadowfacts.extrarails.network.MessageSetColor;
import net.shadowfacts.extrarails.proxy.CommonProxy;
import net.shadowfacts.extrarails.tileentity.TileEntityRailComparator;
import net.shadowfacts.extrarails.tileentity.TileEntityRailTeleporting;

/**
 * @author shadowfacts
 */
@Mod(modid = ExtraRails.MODID, name = ExtraRails.NAME, version = ExtraRails.VERSION, dependencies = "required-after:shadowmc@[3.4.2,);")
public class ExtraRails {

	public static final String MODID = "extrarails";
	public static final String NAME = "Extra Rails";
	public static final String VERSION = "@VERSION@";

	@SidedProxy(serverSide = "net.shadowfacts.extrarails.proxy.CommonProxy", clientSide = "net.shadowfacts.extrarails.proxy.ClientProxy")
	public static CommonProxy proxy;

	@Mod.Instance(MODID)
	public static ExtraRails instance;

	public static SimpleNetworkWrapper network;

//	Content
	public static ModBlocks blocks = new ModBlocks();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ERConfig.init(event.getModConfigurationDirectory());
		ERConfig.load();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());

		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		network.registerMessage(MessageSetColor.Handler.class, MessageSetColor.class, 0, Side.SERVER);
	}

	@Mod.EventBusSubscriber
	public static class EventHandler {

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			event.getRegistry().registerAll(
					blocks.lockingRail,
					blocks.directionRail,
					blocks.teleportingRail,
					blocks.comparatorRail,
					blocks.woodenRail
			);

			GameRegistry.registerTileEntity(TileEntityRailTeleporting.class, "extrarails.teleportingRail");
			GameRegistry.registerTileEntity(TileEntityRailComparator.class, "extrarails.comparatorRail");
		}

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			event.getRegistry().registerAll(
					blocks.lockingRail.createItemBlock(),
					blocks.directionRail.createItemBlock(),
					blocks.teleportingRail.createItemBlock(),
					blocks.comparatorRail.createItemBlock(),
					blocks.woodenRail.createItemBlock()
			);
		}

		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			blocks.lockingRail.initItemModel();
			blocks.directionRail.initItemModel();
			blocks.teleportingRail.initItemModel();
			blocks.comparatorRail.initItemModel();
			blocks.woodenRail.initItemModel();
		}

	}

}
