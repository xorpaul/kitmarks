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

/** A class for constraint checking on Strings.
 * Code taken from Hardcore Java by
 *R. Simmons
 *@author Carmen Wick
 */
public class StringConstraint extends ObjectConstraint{
    private int maxLength;
    private int minLength;

    private String illegalCharacters = "";

    public StringConstraint(final String name, final boolean canBeNull,
			    int minLength, int maxLength)
    {
        super(name, canBeNull);
        if (minLength > maxLength){
            throw new IllegalArgumentException("minValue must be smaller"
                                             + " than maxValue");
        }
        this.maxLength = maxLength;
        this.minLength = minLength;
    }

    public StringConstraint(final String name, final boolean canBeNull,
                            int minLength, int maxLength, 
			    String illegalCharacters)
    {
        this(name, canBeNull, minLength, maxLength);
        this.illegalCharacters = illegalCharacters;
    }

    /** Validates a string.
     *@throws ConstraintException of type VALUE_ABOVE_MAXIMUM if the 
     *string's length is bigger than the maximum length set in this 
     *constraint object,
     *and of type VALUE_BELOW_MINIMUM if the string's length is smaller than
     *minimum length set in this constraint object.
     */
    public void validate(String str){
	super.validate(str);

	if (str == null)
	    return;

	if (str.length() > maxLength){
	    throw new ConstraintException(
		   ConstraintException.VALUE_ABOVE_MAXIMUM,
		   this);
	}
	else if (str.length() < minLength){
	    throw new ConstraintException(
		   ConstraintException.VALUE_BELOW_MINIMUM,
		   this);
	}

    
	for (int i=0 ; i<illegalCharacters.length() ; i++){
	    if (str.indexOf(illegalCharacters.charAt(i)) != -1){
            throw new ConstraintException(
		        ConstraintException.ILLEGAL_CHARACTER,
                this);
	    }
    }

    }

    public int getMinLength(){
        return minLength;
    }

    public int getMaxLength(){
        return maxLength;
    }
}
