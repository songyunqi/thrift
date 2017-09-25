namespace java com.foo.thrift

struct Profile {
    1: string name,
    2: i32 score,
    3: bool enable
}

service ProfileService {
    string updateName(1:Profile profile, 2:string name)
    i32 updateScore(1:Profile profile, 2:i32 score)
    map<string,string> toMap(1:Profile profile)
}