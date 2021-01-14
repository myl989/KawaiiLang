//
//  Token.h
//  KawaiiSwift
//
//  Created by myl on 2021-01-08.
//

#import <Foundation/Foundation.h>

@interface Token : NSObject {
    NSString* type;
    NSObject* value;
}

extern NSString* const TT_INT;
extern NSString* const TT_FLOAT;

extern NSString* const TT_ADD;
extern NSString* const TT_MINUS;
extern NSString* const TT_MUL;
extern NSString* const TT_DIV;
extern NSString* const TT_MOD;
extern NSString* const TT_LPAREN;
extern NSString* const TT_RPAREN;

extern NSString* const TT_EQUALS;
extern NSString* const TT_LT;
extern NSString* const TT_GT;
extern NSString* const TT_LTE;
extern NSString* const TT_GTE;

extern NSString* const TT_ASSIGN;
extern NSString* const TT_VARNAME;
extern NSString* const TT_VARTYPE;
extern NSString* const TT_KEYWORD;

extern NSString* const TT_STARTIF;
extern NSString* const TT_ENDIF;

extern NSString* const TT_PARAM;
extern NSString* const TT_COMMA;

extern NSString* const KEYWORDS[];

extern NSString* const DATA_TYPES[];

- (Token*) init:(NSString*) t value:(NSObject*) v;
- (Token*) init:(NSString*) t;

- (NSString*) description;
- (BOOL)isEqualToToken: (Token*) otherToken;

@end
