package morfologik.stemming;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Attributes applying to {@link Dictionary} and {@link DictionaryMetadata}.
 */
public enum DictionaryAttribute {
  /**
   * Logical fields separator inside the FSA.
   */
  SEPARATOR("fsa.dict.separator") {
    @Override
    public Character fromString(String separator) {
      if (separator == null || separator.length() != 1) {
        throw new IllegalArgumentException("Attribute " + propertyName
            + " must be a single character.");
      }

      char charValue = separator.charAt(0);
      if (Character.isHighSurrogate(charValue) ||
          Character.isLowSurrogate(charValue)) {
        throw new IllegalArgumentException(
            "Field separator character cannot be part of a surrogate pair: " + separator);
      }

      return charValue;
    }
  },

  /**
   * Character to byte encoding used for strings inside the FSA.
   */
  ENCODING("fsa.dict.encoding") {
    @Override
    public Charset fromString(String charsetName) {
      return Charset.forName(charsetName);
    }
  },

  /**
   * If the FSA dictionary includes frequency data.
   */
  FREQUENCY_INCLUDED("fsa.dict.frequency-included") {
    @Override
    public Boolean fromString(String value) {
      return booleanValue(value);
    }
  },

  /**
   * If the spelling dictionary is supposed to ignore words containing digits
   */
  IGNORE_NUMBERS("fsa.dict.speller.ignore-numbers") {
    @Override
    public Boolean fromString(String value) {
      return booleanValue(value);
    }
  },

  /**
   * If the spelling dictionary is supposed to ignore punctuation.
   */
  IGNORE_PUNCTUATION("fsa.dict.speller.ignore-punctuation") {
    @Override
    public Boolean fromString(String value) {
      return booleanValue(value);
    }
  },

  /**
   * If the spelling dictionary is supposed to ignore CamelCase words.
   */
  IGNORE_CAMEL_CASE("fsa.dict.speller.ignore-camel-case") {
    @Override
    public Boolean fromString(String value) {
      return booleanValue(value);
    }
  },

  /**
   * If the spelling dictionary is supposed to ignore ALL UPPERCASE words.
   */
  IGNORE_ALL_UPPERCASE("fsa.dict.speller.ignore-all-uppercase") {
    @Override
    public Boolean fromString(String value) {
      return booleanValue(value);
    }
  },

  /**
   * If the spelling dictionary is supposed to ignore diacritics, so that
   * 'a' would be treated as equivalent to '??'.
   */
  IGNORE_DIACRITICS("fsa.dict.speller.ignore-diacritics") {
    @Override
    public Boolean fromString(String value) {
      return booleanValue(value);
    }
  },

  /**
   * if the spelling dictionary is supposed to treat upper and lower case
   * as equivalent.
   */
  CONVERT_CASE("fsa.dict.speller.convert-case") {
    @Override
    public Boolean fromString(String value) {
      return booleanValue(value);
    }
  },

  /**
   * If the spelling dictionary is supposed to split runOnWords.
   */
  RUN_ON_WORDS("fsa.dict.speller.runon-words") {
    @Override
    public Boolean fromString(String value) {
      return booleanValue(value);
    }
  },

  /** Locale associated with the dictionary. */
  LOCALE("fsa.dict.speller.locale") {
    @Override
    public Locale fromString(String value) {
      return new Locale(value);
    }
  },

  /** Locale associated with the dictionary. */
  ENCODER("fsa.dict.encoder") {
    @Override
    public EncoderType fromString(String value) {
      return EncoderType.valueOf(value.toUpperCase(Locale.ROOT));
    }
  },

  /**
   * Input conversion pairs to replace non-standard characters before search in a speller dictionary.
   * For example, common ligatures can be replaced here.
   */
  INPUT_CONVERSION("fsa.dict.input-conversion") {
    @Override
    public Map<String, String> fromString(String value) throws IllegalArgumentException {
      Map<String, String> conversionPairs = new HashMap<String, String>();
      final String[] replacements = value.split(",\\s*");
      for (final String stringPair : replacements) {
        final String[] twoStrings = stringPair.trim().split(" ");
        if (twoStrings.length == 2) {
          if (!conversionPairs.containsKey(twoStrings[0])) {
            conversionPairs.put(twoStrings[0], twoStrings[1]);
          } else {
            throw new IllegalArgumentException(
                "Input conversion cannot specify different values for the same input string: " + twoStrings[0]);
          }
        } else {
          throw new IllegalArgumentException("Attribute " + propertyName
              + " is not in the proper format: " + value);
        }
      }
      return conversionPairs;
    }
  },

  /**
   * Output conversion pairs to replace non-standard characters before search in a speller dictionary.
   * For example, standard characters can be replaced here into ligatures.
   * 
   * Useful for dictionaries that do have certain standards imposed.
   * 
   */
  OUTPUT_CONVERSION ("fsa.dict.output-conversion") {
    @Override
    public Map<String, String> fromString(String value) throws IllegalArgumentException {
      Map<String, String> conversionPairs = new HashMap<String, String>();
      final String[] replacements = value.split(",\\s*");
      for (final String stringPair : replacements) {
        final String[] twoStrings = stringPair.trim().split(" ");
        if (twoStrings.length == 2) {
          if (!conversionPairs.containsKey(twoStrings[0])) {
            conversionPairs.put(twoStrings[0], twoStrings[1]);
          } else {
            throw new IllegalArgumentException(
                "Input conversion cannot specify different values for the same input string: " + twoStrings[0]);
          }
        } else {
          throw new IllegalArgumentException("Attribute " + propertyName
              + " is not in the proper format: " + value);
        }
      }
      return conversionPairs;
    }
  },

  /**
   * Replacement pairs for non-obvious candidate search in a speller dictionary.
   * For example, Polish <tt>rz</tt> is phonetically equivalent to <tt>??</tt>,
   * and this may be specified here to allow looking for replacements of <tt>rz</tt> with <tt>??</tt>
   * and vice versa.
   */
  REPLACEMENT_PAIRS("fsa.dict.speller.replacement-pairs") {
    @Override
    public Map<String, List<String>> fromString(String value) throws IllegalArgumentException {
      Map<String, List<String>> replacementPairs = new HashMap<String, List<String>>();
      final String[] replacements = value.split(",\\s*");
      for (final String stringPair : replacements) {
        final String[] twoStrings = stringPair.trim().split(" ");
        if (twoStrings.length == 2) {
          if (!replacementPairs.containsKey(twoStrings[0])) {
            List<String> strList = new ArrayList<String>();
            strList.add(twoStrings[1]);
            replacementPairs.put(twoStrings[0], strList);
          } else {
            replacementPairs.get(twoStrings[0]).add(twoStrings[1]);
          }
        } else {
          throw new IllegalArgumentException("Attribute " + propertyName
              + " is not in the proper format: " + value);
        }
      }
      return replacementPairs;
    }
  },

  /**
   * Equivalent characters (treated similarly as equivalent chars with and without
   * diacritics). For example, Polish <tt>??</tt> can be specified as equivalent to <tt>l</tt>.
   * 
   * <p>This implements a feature similar to hunspell MAP in the affix file.
   */
  EQUIVALENT_CHARS("fsa.dict.speller.equivalent-chars") {
    @Override
    public Map<Character, List<Character>> fromString(String value) throws IllegalArgumentException {
      Map<Character, List<Character>> equivalentCharacters =
          new HashMap<Character, List<Character>>();
      final String[] eqChars = value.split(",\\s*");
      for (final String characterPair : eqChars) {
        final String[] twoChars = characterPair.trim().split(" ");
        if (twoChars.length == 2
            && twoChars[0].length() == 1
            && twoChars[1].length() == 1) {
          char fromChar = twoChars[0].charAt(0);
          char toChar = twoChars[1].charAt(0);
          if (!equivalentCharacters.containsKey(fromChar)) {
            List<Character> chList = new ArrayList<Character>();
            equivalentCharacters.put(fromChar, chList);
          }
          equivalentCharacters.get(fromChar).add(toChar);
        } else {
          throw new IllegalArgumentException("Attribute " + propertyName
              + " is not in the proper format: " + value);
        }
      }
      return equivalentCharacters;
    }
  },

  /**
   * Dictionary license attribute.
   */
  LICENSE("fsa.dict.license"),

  /**
   * Dictionary author.
   */
  AUTHOR("fsa.dict.author"),

  /**
   * Dictionary creation date.
   */
  CREATION_DATE("fsa.dict.created");

  /**
   * Property name for this attribute.
   */
  public final String propertyName;

  /**
   * Converts a string to the given attribute's value (covariants used).
   * 
   * @throws IllegalArgumentException
   *             If the input string cannot be converted to the attribute's
   *             value.
   */
  public Object fromString(String value) throws IllegalArgumentException {
    return value;
  }

  /**
   * Return an {@link DictionaryAttribute} by its {@link #propertyName}.
   */
  public static DictionaryAttribute fromPropertyName(String propertyName) {
    DictionaryAttribute value = attrsByPropertyName.get(propertyName);
    if (value == null) {
      throw new IllegalArgumentException("No attribute for property: " + propertyName);
    }
    return value;
  }

  private static final Map<String,DictionaryAttribute> attrsByPropertyName;
  static {
    attrsByPropertyName = new HashMap<String,DictionaryAttribute>();
    for (DictionaryAttribute attr : DictionaryAttribute.values()) {
      if (attrsByPropertyName.put(attr.propertyName, attr) != null) {
        throw new RuntimeException("Duplicate property key for: " + attr);
      }
    }
  }

  /**
   * Private enum instance constructor.
   */
  private DictionaryAttribute(String propertyName) {
    this.propertyName = propertyName;
  }

  private static Boolean booleanValue(String value) {
    value = value.toLowerCase();
    if ("true".equals(value) || "yes".equals(value) || "on".equals(value)) {
      return Boolean.TRUE;
    }
    if ("false".equals(value) || "no".equals(value) || "off".equals(value)) {
      return Boolean.FALSE;
    }
    throw new IllegalArgumentException("Not a boolean value: " + value);
  }
}
