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

- (Token*) init:(NSString*) t value:(NSObject*) v;
- (Token*) init:(NSString*) t;

- (NSString*) description;
- (BOOL)isEqualToToken: (Token*) otherToken;

@end
