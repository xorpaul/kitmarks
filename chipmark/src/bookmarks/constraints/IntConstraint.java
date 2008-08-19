/*
-------------------------------------------------------------------------------------------------
Copyright, 2006, Chipmark.

This file is part of Chipmark.

Chipmark is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Chipmark is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Chipmark; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
-------------------------------------------------------------------------------------------------
*/

package bookmarks.constraints;

/** A class for bounds checking on integer primitives. 
 *Code taken from Hardcore Java by
 *R. Simmons
 *@author Carmen Wick
 */
public class IntConstraint extends Constraint{
    private int minValue;
    private int maxValue;

    public IntConstraint(final String name, final int minValue,
			 final int maxValue)
    {
	super(name);
	if (minValue > maxValue){
	    throw new IllegalArgumentException("minValue must be smaller"
					       + " than maxValue");
	}
	this.minValue = minValue;
	this.maxValue = maxValue;
    }

    public int getMaxValue(){
	return maxValue;
    }

    public int getMinValue(){
	return minValue;
    }

    public void validate(final int value){
	if (value < minValue){
	    throw new ConstraintException(
	           ConstraintException.VALUE_BELOW_MINIMUM,
		   this);
	}
	if (value > maxValue){
	    throw new ConstraintException(
      		   ConstraintException.VALUE_ABOVE_MAXIMUM,
		   this);
	}
    }

}
