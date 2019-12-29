package vonzeeple.animagrowth;

import growthcraft.bees.common.lib.config.BeesFluidTag;
import growthcraft.bees.shared.init.GrowthcraftBeesFluids;
import growthcraft.bees.shared.init.GrowthcraftBeesItems;
import growthcraft.cellar.shared.GrowthcraftCellarApis;
import growthcraft.cellar.shared.booze.BoozeTag;
import growthcraft.cellar.shared.config.GrowthcraftCellarConfig;
import growthcraft.core.shared.CoreRegistry;
import growthcraft.core.shared.config.GrowthcraftCoreConfig;
import growthcraft.core.shared.fluids.FluidTag;
import growthcraft.core.shared.item.OreItemStacks;
import growthcraft.core.shared.utils.TickUtils;
import growthcraft.milk.shared.MilkRegistry;
import growthcraft.milk.shared.definition.KumisTypes;
import growthcraft.milk.shared.fluids.MilkFluidTags;
import growthcraft.milk.shared.init.GrowthcraftMilkFluids;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import static growthcraft.milk.common.Init.roundToBottles;

@Mod(modid = AnimaGrowth.MODID, name = AnimaGrowth.NAME, version = AnimaGrowth.VERSION,
        dependencies = "required-after:growthcraft_bees;required-after:growthcraft_milk;required-after:animania")
public class AnimaGrowth
{
    public static final String MODID = "animagrowth";
    public static final String NAME = "AnimaGrowth";
    public static final String VERSION = "0.1";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        String[] milk_names = {"milk_holstein","milk_friesian","milk_jersey","milk_goat","milk_sheep"};
        int restCapRounded = roundToBottles(1000 - 2 * GrowthcraftCoreConfig.bottleCapacity);
        int fermentTime = GrowthcraftCellarConfig.fermentTime;

        //Honey recipes
        if(ModConfig.config_honey) {
            if (FluidRegistry.isFluidRegistered("animania_honey")) {
                logger.info("Registering animania_honey");
                CoreRegistry.instance().fluidDictionary().addFluidTags(FluidRegistry.getFluid("animania_honey"), new FluidTag[]{BeesFluidTag.HONEY});

                GrowthcraftCellarApis.boozeBuilderFactory.create(GrowthcraftBeesFluids.meadBooze[GrowthcraftBeesItems.MeadTypes.MEAD_YOUNG.ordinal()].getFluid()).tags(new FluidTag[]{BeesFluidTag.MEAD, BoozeTag.YOUNG}).fermentsFrom(new FluidStack(FluidRegistry.getFluid("animania_honey"), 250), new OreItemStacks("yeastBrewers"), fermentTime);
            }
        }

        //Milk recipes
        for(String milk : milk_names){
            if(FluidRegistry.isFluidRegistered(milk)) {
                logger.info("Registering " + milk);
                //Add pancheon recipe
                if(ModConfig.config_pancheon) {
                    MilkRegistry.instance().pancheon().addRecipe(new FluidStack(FluidRegistry.getFluid(milk), 1000), GrowthcraftMilkFluids.cream.asFluidStack(2 * GrowthcraftCoreConfig.bottleCapacity), GrowthcraftMilkFluids.skimMilk.asFluidStack(restCapRounded), TickUtils.minutes(1));
                }
                //Add kumi recipe
                if(ModConfig.config_milk) {
                    //Add milk tag to enable vat recipes
                    CoreRegistry.instance().fluidDictionary().addFluidTags(FluidRegistry.getFluid(milk), new FluidTag[]{MilkFluidTags.MILK});
                    GrowthcraftCellarApis.boozeBuilderFactory.create(GrowthcraftMilkFluids.kumisBooze[KumisTypes.KUMIS_FERMENTED.ordinal()].getFluid()).tags(new FluidTag[]{BoozeTag.KUMIS, BoozeTag.FERMENTED}).fermentsFrom(new FluidStack(FluidRegistry.getFluid(milk), 200), new ItemStack(Items.NETHER_WART), fermentTime);
                }
            }else{
                logger.info("Fluid " + milk+" not found");
            }
        }


    }
}
