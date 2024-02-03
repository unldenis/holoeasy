/*
 * Hologram-Lib - Asynchronous, high-performance Minecraft Hologram
 * library for 1.8-1.18 servers.
 * Copyright (C) unldenis <https://github.com/unldenis>
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
package org.holoeasy.util

import org.bukkit.Location
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.sqrt

/**
 * Represents an axix-aligned bounding box.
 *
 * @author Kristian
 * @author unldenis
 */
class AABB
/**
 * Creates a new instance from a minimum point and a maximum point.
 */(private var min: Vec3D, private var max: Vec3D) {
    /**
     * Create a new com.github.unldenis.hologram.util.AABB from a given block.
     *
     * @param block - the block.
     */
    constructor(block: Location) : this(Vec3D.fromLocation(block), Vec3D.fromLocation(block).add(Vec3D.UNIT_MAX))

    /**
     * Translate this AABB to a given point.
     *
     * @param vec - the point.
     */
    fun translate(vec: Vec3D) {
        this.min = min.add(vec)
        this.max = max.add(vec)
    }

    /**
     * Calculates intersection with the given ray between a certain distance interval.
     *
     *
     * Ray-box intersection is using IEEE numerical properties to ensure the test is both robust and
     * efficient, as described in:
     * <br></br>
     * `Amy Williams, Steve Barrus, R. Keith Morley, and Peter Shirley: "An
     * Efficient and Robust Ray-Box Intersection Algorithm" Journal of graphics tools, 10(1):49-54,
     * 2005`
     *
     * @param ray incident ray
     * @return intersection point on the bounding box (only the first is returned) or null if no
     * intersection
     */
    fun intersectsRay(ray: Ray3D, minDist: Float, maxDist: Float): Vec3D? {
        val invDir = Vec3D(1f / ray.direction.x, 1f / ray.direction.y, 1f / ray.direction.z)

        val signDirX = invDir.x < 0
        val signDirY = invDir.y < 0
        val signDirZ = invDir.z < 0

        var bbox = if (signDirX) max else min
        var tmin = (bbox.x - ray.x) * invDir.x
        bbox = if (signDirX) min else max
        var tmax = (bbox.x - ray.x) * invDir.x
        bbox = if (signDirY) max else min
        val tymin = (bbox.y - ray.y) * invDir.y
        bbox = if (signDirY) min else max
        val tymax = (bbox.y - ray.y) * invDir.y

        if ((tmin > tymax) || (tymin > tmax)) {
            return null
        }
        if (tymin > tmin) {
            tmin = tymin
        }
        if (tymax < tmax) {
            tmax = tymax
        }

        bbox = if (signDirZ) max else min
        val tzmin = (bbox.z - ray.z) * invDir.z
        bbox = if (signDirZ) min else max
        val tzmax = (bbox.z - ray.z) * invDir.z

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null
        }
        if (tzmin > tmin) {
            tmin = tzmin
        }
        if (tzmax < tmax) {
            tmax = tzmax
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin)
        }
        return null
    }

    open class Vec3D {
        /**
         * X coordinate.
         */
        val x: Double

        /**
         * Y coordinate.
         */
        val y: Double

        /**
         * Z coordinate.
         */
        val z: Double

        /**
         * Creates a new vector with the given coordinates.
         *
         * @param x the x
         * @param y the y
         * @param z the z
         */
        constructor(x: Double, y: Double, z: Double) {
            this.x = x
            this.y = y
            this.z = z
        }

        /**
         * Creates a new vector with the coordinates of the given vector.
         *
         * @param v vector to copy.
         */
        constructor(v: Vec3D) {
            this.x = v.x
            this.y = v.y
            this.z = v.z
        }

        /**
         * Add vector v and returns result as new vector.
         *
         * @param v vector to add
         * @return result as new vector
         */
        fun add(v: Vec3D): Vec3D {
            return Vec3D(x + v.x, y + v.y, z + v.z)
        }

        /**
         * Scales vector uniformly and returns result as new vector.
         *
         * @param s scale factor
         * @return new vector
         */
        fun scale(s: Double): Vec3D {
            return Vec3D(x * s, y * s, z * s)
        }

        /**
         * Normalizes the vector so that its magnitude = 1.
         *
         * @return The normalized vector.
         */
        fun normalize(): Vec3D {
            val mag = sqrt(x * x + y * y + z * z)

            if (mag > 0) {
                return scale(1.0 / mag)
            }
            return this
        }

        override fun equals(obj: Any?): Boolean {
            if (obj is Vec3D) {
                val v = obj
                return x == v.x && y == v.y && z == v.z
            }
            return false
        }

        override fun hashCode(): Int {
            return Objects.hash(x, y, z)
        }

        override fun toString(): String {
            return String.format("{x: %g, y: %g, z: %g}", x, y, z)
        }

        companion object {
            /**
             * Point with the coordinate (1, 1, 1).
             */
            val UNIT_MAX: Vec3D = Vec3D(1.0, 1.0, 1.0)

            /**
             * Construct a vector from a Bukkit location.
             *
             * @param loc - the Bukkit location.
             */
            @JvmStatic
            fun fromLocation(loc: Location): Vec3D {
                return Vec3D(loc.x, loc.y, loc.z)
            }

            /**
             * Construct a copy of our immutable vector from Bukkit's mutable vector.
             *
             * @param v - Bukkit vector.
             * @return A copy of the given vector.
             */
            fun fromVector(v: Vector): Vec3D {
                return Vec3D(v.x, v.y, v.z)
            }
        }
    }

    class Ray3D(origin: Vec3D, direction: Vec3D) : Vec3D(origin) {
        val direction: Vec3D = direction.normalize()

        /**
         * Construct a 3D ray from a location.
         *
         * @param loc - the Bukkit location.
         */
        constructor(loc: Location) : this(fromLocation(loc), fromVector(loc.direction))

        fun getPointAtDistance(dist: Double): Vec3D {
            return add(direction.scale(dist))
        }

        override fun toString(): String {
            return "origin: " + super.toString() + " dir: " + direction
        }
    }
}