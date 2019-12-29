package vonzeeple.animagrowth;
import net.minecraftforge.common.config.Config;

@Config(modid = AnimaGrowth.MODID)
public class ModConfig {
    @Config.Name("Pancheon")
    @Config.Comment("Allow pancheon to work with Animania milk")
    public static boolean config_pancheon = true;
    @Config.Name("Honey")
    @Config.Comment("Allow fermentation of Animania honey")
    public static boolean config_honey = true;
    @Config.Name("Kumi")
    @Config.Comment("Allow fermentation of Animania milk")
    public static boolean config_milk = true;
}
