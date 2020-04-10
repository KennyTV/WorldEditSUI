/*
 * WorldEditSUI - https://git.io/wesui
 * Copyright (C) 2018-2020 KennyTV (https://github.com/KennyTV)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.kennytv.worldeditsui.compat.we6;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import eu.kennytv.worldeditsui.compat.ProtectedRegionHelper;
import eu.kennytv.worldeditsui.compat.ProtectedRegionWrapper;
import eu.kennytv.worldeditsui.compat.SelectionType;
import org.bukkit.World;

import java.util.Collections;
import java.util.Set;

public final class ProtectedRegionHelper6 implements ProtectedRegionHelper {

    private final RegionContainer regionContainer = WorldGuardPlugin.inst().getRegionContainer();

    @Override
    public ProtectedRegionWrapper getRegion(final World world, final String regionName) {
        final RegionManager regionManager = regionContainer.get(world);
        if (regionManager == null) return null;

        final ProtectedRegion region = regionManager.getRegion(regionName);
        final RegionType type = region.getType();
        final com.sk89q.worldedit.world.World worldWrapper = new BukkitWorld(world);
        switch (type) {
            case CUBOID:
                return new ProtectedRegionWrapper(new CuboidRegion(worldWrapper, region.getMinimumPoint(), region.getMaximumPoint()), SelectionType.CUBOID);
            case POLYGON:
                return new ProtectedRegionWrapper(new Polygonal2DRegion(worldWrapper, region.getPoints(),
                        (int) region.getMinimumPoint().getY(), (int) region.getMaximumPoint().getY()), SelectionType.POLYGON);
            case GLOBAL:
            default:
                return null;
        }
    }

    @Override
    public Set<String> getRegionNames(final World world) {
        final RegionManager regionManager = regionContainer.get(world);
        if (regionManager == null) return Collections.emptySet();

        return regionManager.getRegions().keySet();
    }
}
