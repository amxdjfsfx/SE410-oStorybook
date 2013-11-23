/*
Storybook: Open Source software for novelists and authors.
Copyright (C) 2008 - 2012 Martin Mustun

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package storybook.model.hbn.entity;

import storybook.toolkit.I18N;
import storybook.toolkit.TextUtil;

/**
 * Chapter generated by hbm2java
 *
 * @hibernate.class table="CHAPTER"
 */
public class Chapter extends AbstractEntity implements Comparable<Chapter> {

	private static final long serialVersionUID = -6581550047607846080L;

	private Part part;
	private Integer chapterno;
	private String title;
	private String description;
	private String notes;

	public Chapter() {
	}

	public Chapter(Part part, Integer chapterno, String title,
			String description, String notes) {
		this.part = part;
		this.chapterno = chapterno;
		this.title = title;
		this.description = description;
		this.notes = notes;
	}

	/**
	 * @hibernate.id
	 *   column="ID"
	 *   generator-class="increment"
	 *   unsaved-value="null"
	 */
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @hibernate.many-to-one
	 *   column="part_id"
	 *   cascade="none"
	 */
	public Part getPart() {
		return part;
	}

	public boolean hasPart() {
		return part != null;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	/**
	 * @hibernate.property
	 */
	public Integer getChapterno() {
		return this.chapterno == null ? -1 : this.chapterno;
	}

	public void setChapterno(Integer chapterno) {
		this.chapterno = chapterno;
	}

	public String getChapternoStr() {
		return getChapterno().toString();
	}

	/**
	 * @hibernate.property
	 */
	public String getTitle() {
		return this.title == null ? "" : this.title;
	}

	public String getTitle(boolean truncate) {
		return title == null ? "" : TextUtil.truncateString(title, 30);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @hibernate.property
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @hibernate.property
	 */
	public String getNotes() {
		if (notes == null) {
			return "";
		}
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		if (chapterno == null) {
			return I18N.getMsg("msg.unassigned.scenes");
		}
		return getChapterno() + ": " + getTitle();
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		Chapter test = (Chapter) obj;
		boolean ret = true;
		ret = ret && equalsObjectNullValue(part, test.getPart());
		ret = ret && equalsIntegerNullValue(chapterno, test.getChapterno());
		ret = ret && equalsStringNullValue(title, test.getTitle());
		ret = ret && equalsStringNullValue(description, test.getDescription());
		ret = ret && equalsStringNullValue(notes, test.getNotes());
		return ret;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = hash * 31 + part.hashCode();
		hash = hash * 31 + chapterno.hashCode();
		hash = hash * 31 + title.hashCode();
		hash = hash * 31 + description.hashCode();
		hash = hash * 31 + notes.hashCode();
		return hash;
	}

	@Override
	public int compareTo(Chapter ch) {
		return chapterno.compareTo(ch.chapterno);
	}
}
