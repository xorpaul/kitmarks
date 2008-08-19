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

/** An exception which is thrown when incoming data to a servlet
 * fails bounds checking
 *@author Carmen Wick
 */
public class ConstraintException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2435680929962966853L;
	
	public static final int VALUE_BELOW_MINIMUM=0;
    public static final int VALUE_ABOVE_MAXIMUM=1;
    public static final int NULL_NOT_ALLOWED=2;
    public static final int ILLEGAL_CHARACTER=3;

    private int type;
    private Constraint constraint;

    public ConstraintException(final int type, final Constraint constraint){
	this.type = type;
	this.constraint = constraint;
    }

    public int getType(){ 
	return type;
    }

    public Constraint getConstraint(){
	return constraint;
    }
}
