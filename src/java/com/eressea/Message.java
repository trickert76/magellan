// ===
// Copyright (C) 2000, 2001 Roger Butenuth, Andreas Gampe, Stefan G�tz, Sebastian Pappert, Klaas Prause, Enno Rehling, Sebastian Tusk
// ---
// This file is part of the Eressea Java Code Base, see the file LICENSING for the licensing information applying to this file
// ---
// $Id$
// ===

package com.eressea;

import java.util.Map;
import java.util.StringTokenizer;

import com.eressea.rules.MessageType;
import com.eressea.util.logging.Logger;

/**
 * A class for representing a message.
 * <p>The new format of messages in Eressea CR versions >= 41 made
 * it necessary to reconsider this class. Mainly, messages can now
 * have an id and attributes.</p>
 * <p>Two special attributes are available
 * directly via the corresping get/set methods.</p>
 * <p>First, this is the type attribte (tag ;type in the cr) 
 * denoting the type of the message. It is transformed into a 
 * <tt>MessageType</tt> object.</p>
 * <p>Second, there is the text attribute (tag ;rendered in the cr).
 * By design this attribute should actually be created by rendering
 * the message according to the message type's pattern and the other
 * attributes. This class does contain a <tt>render()</tt> method,
 * still, it is too primitive to yield acceptable results, so it is
 * preferrable to take the rendered message text directly from the cr.
 * </p>
 * <p>Note, that for historic reasons, a <tt>Message</tt> object might
 * have no type or attributes and an invalid id of -1.</p>
 */
public class Message extends Identifiable {
	private final static Logger log = Logger.getInstance(Message.class);
	private String text = null;
	private MessageType type = null;
	/**
	 * The attributes of this message. The keys are the keys of the
	 * attribute, the values object pairs of the attributes' keys and
	 * values.
	 */
	public Map attributes = null;

	/* 
	 * this is a helper static variable so we only have one reference (tricky, eh?) 
	 */
	private final static IntegerID ambiguousID = IntegerID.create(-1);
	public Message(String text) {
		this(ambiguousID,text,null,null);
	}
	
	public Message(ID id) {
		this(id, null, null, null);
	}

	public Message(ID id, String text) {
		this(id, text, null, null);
	}

	public Message(ID id, MessageType type, Map attributes) {
		this(id, null, type, attributes);
	}
	
	public Message(Message msg) {
		this(msg.getID(), msg.getText(), msg.getType(), msg.attributes);
	}

	public Message(ID id, String text, MessageType type, Map attributes) {
		super(id);
		if (text == null && type != null) {
			this.text = render(null, type.getPattern(), attributes);
		} else {
			this.text = text;
		}
		this.type = type;
		if (attributes != null) {
			this.attributes = new com.eressea.util.OrderedHashtable(attributes);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public MessageType getType() {
		return type;
	}
	
	public void setType(MessageType type) {
		this.type = type;
	}
	
	/**
	 * Helper method for render. Translates attributes into user strings.
	 */
	private static String renderAttribute(GameData data, String id, String attribute) {
		
		if ( id.equalsIgnoreCase("region") ) {
			Coordinate coord = Coordinate.parse(attribute, ",");
			if (coord == null) {
				coord = Coordinate.parse(attribute, " ");
			}
			return data.getRegion(coord).toString();
		}
		if ( id.equalsIgnoreCase("unit") || id.equalsIgnoreCase("target") ) {
			Unit unit = null;
			if (attribute != null) {
				unit = data.getUnit(new UnitID(attribute, 10));
			}
			return unit == null ? null : unit.toString();
		}
		
		return attribute;
	}
	
	public static String render(GameData data, String pattern, Map attributes) {
		if (pattern == null || attributes == null) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		String token = "", lastToken = "";
		StringTokenizer st = new StringTokenizer(pattern, "{}", true);
		while (st.hasMoreTokens()) {
			lastToken = token;
			token = st.nextToken();
			if (lastToken.equals("{")) {
				StringTokenizer tt = new StringTokenizer(token);
				while (tt.hasMoreTokens()) {
					String strToken = tt.nextToken();
					String value = (String)attributes.get(strToken);
					String strAttribute = renderAttribute(data, strToken, value);
					sb.append( strAttribute );
				}
			} else {
				if (!token.equals("{") && !token.equals("}")) {
					sb.append(token);
				}
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Renders the message and updates the message text.
	 */
	public void render( GameData data ) {
		setText( Message.render(data, type.getPattern(), attributes) );
	}


	public int hashCode() {
		// identify Message by message text
		return text == null ? super.hashCode(): text.hashCode();
	}
	/**
	 * Indicates whether this Message object is equal to another
	 * object. Returns true only if o is not null and an instance of
	 * class Message and o's id is equal to the id of this 
	 * Message object.
	 * 2002.02.21 pavkovic: Also the message text has to be the same for Messages 
	 * with ambiguous IntegerID(-1) 
	 */
	public boolean equals(Object o) {
		if (o instanceof Message) {
			boolean ret = this.getID().equals(ambiguousID) ? 
				isPrimitiveEquals((Message) o) :
				isComplexEquals((Message) o);
			/*
			if(ret && log.isDebugEnabled()) {
				log.debug("Messages: "+this+", "+o);
				log.debug("this.getID() == ambiguousID:" +(this.getID().equals(ambiguousID)));
				log.debug("isPrimitiveEquals:"+isPrimitiveEquals((Message) o));
				log.debug("isComplexEquals:"+isComplexEquals((Message) o));
			} 
			*/
			return ret;

		} else {
			return false;
		}
	}
	
	/** 
	 * This checks if Messages are of old style without id.
	 * In such a situation we have to compare the text
	 */
	private boolean isPrimitiveEquals(Message o) {
		// we use == for ambiguousID as it is singleton
		// this is true iff this.ID == o.ID == ambiguousID && this.text == o.text
		return this.getID() == o.getID() &&
			equalObjects(this.getText(),o.getText());
	}
		
	/** 
	 * This checks if Messages are of new style (CR version >= 41 with id.
	 * But the id is not an identifying characteristica.
	 */
	private boolean isComplexEquals(Message o) {
		// this means: this.ID == o.ID ( != ambiguousID ) || (<IDs are not equal> this.text == o.text && this.messageType == o.messageType)
		return 	!this.getID().equals(ambiguousID) && (this.getID().equals(o.getID()) || (equalObjects(this.getText(),o.getText()) && equalObjects(this.getType(),o.getType())));
	}
	
	private final static boolean equalObjects(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}
	

	/**
	 * Imposes a natural ordering on Message objects equivalent to
	 * the natural ordering of their ids.
	 */
	public int compareTo(Object o) {
		return this.getID().compareTo(((Message)o).getID());
	}
	
	/**
	 * Transfers all available information from the current message
	 * to the new one.
	 *
	 * @param curGD fully loaded game data
	 * @param curGroup a fully initialized and valid message
	 * @param newGD the game data to be updated
	 * @param newGroup a message to be updated with the data from
	 * curMsg
	 */
	public static void merge(GameData curGD, Message curMsg, GameData newGD, Message newMsg) {
		if (curMsg.attributes != null && curMsg.attributes.size() > 0) {
			if (newMsg.attributes == null) {
				newMsg.attributes = new com.eressea.util.OrderedHashtable();
			} else {
				newMsg.attributes.clear();
			}
			newMsg.attributes.putAll(curMsg.attributes);
		}
		if (curMsg.getText() != null) {
			newMsg.setText(curMsg.getText());
		}
		if (curMsg.getType() != null) {
			newMsg.setType(newGD.getMsgType(curMsg.getType().getID()));
		}
	}

	public String toString() {
		return "Message["+
			"id="+id+","+
			"type="+(type == null ? "null": (type.getID()+""))+","+
			"text=\""+text+"\","+
			"attributes="+attributes+
			"]";
	}
}
